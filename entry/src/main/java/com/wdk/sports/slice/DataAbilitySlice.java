package com.wdk.sports.slice;

import com.andexert.calendarlistview.library.DayPickerView;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.anan.AAChartCore.utils.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lxj.xpopup.util.ToastUtil;
import com.wdk.sports.ResourceTable;
import com.wdk.sports.domain.Count;
import com.wdk.sports.domain.CountSportData;
import com.wdk.sports.domain.ResultVO;
import com.wdk.sports.domain.SportDataAddId;
import com.wdk.sports.util.Constans;
import com.wdk.sports.util.StringUtils;
import com.zhy.http.library.OkHttpUtils;
import com.zhy.http.library.callback.StringCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import okhttp3.Call;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DataAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Button dataBackBtn = null;
    private Image showCalendarImage = null;
    private CommonDialog calendarCommonDialog = null;
    private DayPickerView dayPickerView = null;
    private Button cancelCalendarBtn;
    private Button okCalendarBtn;

    private String year = LocalDate.now().getYear() + "";
    private String month = LocalDate.now().getMonthValue() + "";
    private String day = LocalDate.now().getDayOfMonth() + "";

    private Text dateText;
    private Text calendarDateText;
    private Button todayBtn;
    private TableLayout calendarView;
    private Image lastImage;
    private Image nextImage;

    private DirectionalLayout dateDay;
    private DirectionalLayout dateWeek;
    private DirectionalLayout dateMonth;

    private Image last_day_week_month;
    private Image next_day_week_month;

    private int currentTimeMode = 1 ; // 1(默认) -> 日 2 -> 周 3 -> 月
    private String device_id; // 设备id
    private String device_type; // 设备型号

    private AAChartView  aaChartView02;
    private AAChartView  aaChartView01;

    private String[] categoriesQuanShu = {"", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
    private AAChartModel aaChartModel;

    private Text showDataText; // 显示数据的文字
    private Text maxSpeedText;
    private Text averageSpeedText;
    private Text roundNumberText;
    private Text interruptNumberText;
    private Text minText;
    private Text secondText;


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent( ResourceTable.Layout_ability_page_data_y );

        device_id = (String) intent.getParams().getParam("device_id");
        device_type = (String) intent.getParams().getParam("device_type");

        initComponent();
        initComponentListener();

    }

    private void initComponentListener() {
        dataBackBtn.setClickedListener(this);
        showCalendarImage.setClickedListener( this );
        dateDay.setClickedListener( this );
        dateWeek.setClickedListener( this );
        dateMonth.setClickedListener( this );

        last_day_week_month.setClickedListener( this );
        next_day_week_month.setClickedListener( this );
    }

    private void initComponent() {

        Calendar calendar = Calendar.getInstance();
        String format = simpleDateFormat.format(calendar.getTime());
        this.year = format.split("-")[0];
        this.month = format.split("-")[1];
        this.day = format.split("-")[2];

        dataBackBtn = (Button) this.findComponentById(ResourceTable.Id_btn_data_back);
        showCalendarImage = (Image) this.findComponentById(ResourceTable.Id_image_show_calendar);
        dateText = (Text) this.findComponentById(ResourceTable.Id_text_showDate);

        dateText.setText(year + "年" + month + "月" + day + "日");
        String start = year + "-" + month + "-" + day ;
        appData_count( start,"" );
//        appData_count( "2023-03-13","" );

        // 日周月
        dateDay = (DirectionalLayout) this.findComponentById(ResourceTable.Id_date_day);
        dateWeek = (DirectionalLayout) this.findComponentById(ResourceTable.Id_date_week);
        dateMonth = (DirectionalLayout) this.findComponentById(ResourceTable.Id_date_month);

        dateWeek.getComponentAt(1).setVisibility( Component.HIDE );
        dateMonth.getComponentAt(1).setVisibility( Component.HIDE );

        last_day_week_month = (Image) this.findComponentById(ResourceTable.Id_last_day_week_month);
        next_day_week_month = (Image) this.findComponentById(ResourceTable.Id_next_day_week_month);

        aaChartView02 = (AAChartView) this.findComponentById(ResourceTable.Id_AAChartView02);
        aaChartView01 = (AAChartView) this.findComponentById(ResourceTable.Id_AAChartView01);

        showDataText = (Text) this.findComponentById(ResourceTable.Id_text_showdata);
        String str = "当天数据(" + (LocalTime.now().getHour() > 12 ?
                "下午" + (LocalTime.now().getHour()-12) + ":" + (LocalTime.now().getMinute()>=10?LocalTime.now().getMinute(): "0" + LocalTime.now().getMinute() ) + ")"
                :"上午" + LocalTime.now().getHour()+ ":" + (LocalTime.now().getMinute()>=10?LocalTime.now().getMinute(): "0" + LocalTime.now().getMinute() ) + ")") ;
        showDataText.setText( str );

        averageSpeedText = (Text) this.findComponentById(ResourceTable.Id_text_average_speed);
        maxSpeedText = (Text) this.findComponentById(ResourceTable.Id_text_max_speed);
        interruptNumberText = (Text) this.findComponentById(ResourceTable.Id_text_interrupt_number);
        roundNumberText = (Text) this.findComponentById(ResourceTable.Id_text_round_number);
        minText = (Text) this.findComponentById(ResourceTable.Id_text_min);
        secondText = (Text) this.findComponentById(ResourceTable.Id_text_second);
    }


    /**
     * 数据统计
     */
    private void appData_count(String start, String end) {
        if (StringUtils.isEmpty( end )){
            appData_count( start );
            return;
        }

        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .get()
                    .addParams("device_id", device_id)
                    .addParams("device_type", device_type)
                    .addParams("start",start)
                    .addParams("end",end)
                    .addHeader("time", new Date().getTime() +"" )
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appData/count")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("数据统计失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            ResultVO<CountSportData> resultVO = new Gson().fromJson(s, new TypeToken<ResultVO<CountSportData>>(){}.getType());
                            List<SportDataAddId> list = resultVO.getData().getList();

                            Integer[] data = null; // 运动时长
                            Integer[] data2 = null; // 圈数
                            if ( currentTimeMode == 2 ){ // 星期
                                data = new Integer[7];
                                data2 = new Integer[7];
                                for (int j = 0; j < data.length; j++) {
                                    data[j] = new Integer(0);
                                    data2[j] = new Integer(0);
                                }

                                List<String> categories = new ArrayList();
                                categories.add(
                                        start.substring(5,7).startsWith("0")?
                                                start.substring(6,7) + "/" + start.substring(8)
                                                : start.substring(5,7) +"/" + start.substring(8));
                                try {
                                    Date startDate = simpleDateFormat.parse(start);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime( startDate );

                                    for (int j = 1; j < 7; j++) {
                                        calendar.add( Calendar.DAY_OF_MONTH, 1);
                                        String format = simpleDateFormat.format(calendar.getTime());
                                        categories.add( format.substring(5,7).startsWith("0")?
                                                format.substring(6,7) + "/" + format.substring(8)
                                                : format.substring(5,7) + "/" + format.substring(8));
                                    }

                                    int sum_average_speed = 0;// 总秒数
                                    double max_average_speed = 0.0;
                                    int interruptNumber = 0;
                                    int sum_second = 0;
                                    int counts = 0 ;
                                    for (SportDataAddId sportDataAddId : list) {
                                        String time = sportDataAddId.getTime();
                                        String s1 = time.substring(5,7).startsWith("0")?
                                                time.substring(6,7) + "/" + time.substring(8,10)
                                                : time.substring(5,7) + "/" + time.substring(8,10);

                                        int i1 = categories.indexOf(s1);
                                        data[ i1 ] += sportDataAddId.getDuration() ;
                                        data2[ i1 ] += sportDataAddId.getCounts() ;

                                        sum_average_speed += sportDataAddId.getAverage_speed() ;
                                        sum_second += sportDataAddId.getDuration();
                                        max_average_speed = sportDataAddId.getAverage_speed() > max_average_speed ? sportDataAddId.getAverage_speed():max_average_speed;
                                        interruptNumber += sportDataAddId.getInterrupt_count();
                                        counts += sportDataAddId.getCounts() ;
                                    }

                                    // 显示图表
                                    // 时长
                                    drawLineChartQuanShu( data ,categories.toArray( new String[categories.size()]), "次", new String[]{"#64BB5C"});
                                    aaChartView01.aa_drawChartWithChartModel( aaChartModel );
                                    // 圈数
                                    drawLineChartQuanShu( data2, categories.toArray( new String[categories.size()]), "圈", new String[]{"#46B1E3"});
                                    aaChartView02.aa_drawChartWithChartModel( aaChartModel );

                                    // 运动数据显示
                                    // 平均速度
                                    DirectionalLayout parent = (DirectionalLayout) minText.getComponentParent();
                                    Text text1 = (Text) parent.getComponentAt(1);
                                    Text text2 = (Text) parent.getComponentAt(3);

                                    DirectionalLayout parent1 = (DirectionalLayout) interruptNumberText.getComponentParent();
                                    Text text3 = (Text) parent1.getComponentAt(1);

                                    DirectionalLayout parent2 = (DirectionalLayout) maxSpeedText.getComponentParent();
                                    Text text4 = (Text) parent2.getComponentAt(1);

                                    DirectionalLayout parent3 = (DirectionalLayout) averageSpeedText.getComponentParent();
                                    Text text5 = (Text) parent3.getComponentAt(1);
                                    if ( list.size() <= 0 ){
                                        averageSpeedText.setText("--");
                                        maxSpeedText.setText("--");
                                        interruptNumberText.setText("--");
                                        roundNumberText.setText("--");
                                        minText.setText("-");
                                        secondText.setText("-");

                                        // 下右
                                        text1.setText("");
                                        text2.setText("");

                                        // 下左
                                        text3.setText("");

                                        // 上右
                                        text4.setText("");

                                        // 上左
                                        text5.setText("");
                                        return ;
                                    }
                                    // 下右
                                    text1.setText("分");
                                    text2.setText("秒");
                                    // 下左
                                    text3.setText("次");
                                    // 上右
                                    text4.setText("圈/分钟");
                                    // 上左
                                    text5.setText("圈/分钟");

                                    int average_speed = sum_average_speed / list.size() ;
                                    averageSpeedText.setText(  average_speed + "");

                                    // 最大速度
                                    maxSpeedText.setText( (int)max_average_speed + "");

                                    // 绊绳数
                                    interruptNumberText.setText( interruptNumber + "");

                                    // 转动圈数
                                    roundNumberText.setText( counts + "" );

                                    // 运动时长（分，秒）
                                    minText.setText( sum_second / 60 + "");
                                    secondText.setText( sum_second % 60 + "");

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if ( currentTimeMode == 3 ){ // 月
                                try {
                                    Date startDate = simpleDateFormat.parse(start);
                                    Date endDate = simpleDateFormat.parse(end);

                                    // 两个日期之间的天数
                                    int difference = (int) ((endDate.getTime() - startDate.getTime()) / 86400000) + 1;

                                    // 初始化data[]
                                    data = new Integer[difference];
                                    data2 = new Integer[difference];
                                    for (int j = 0; j < data.length; j++) {
                                        data[j] = new Integer(0);
                                        data2[j] = new Integer(0);
                                    }

                                    // 生成categories
                                    List<String> categories = new ArrayList();
                                    categories.add(
                                            start.substring(5,7).startsWith("0")?
                                                    start.substring(6,7) + "/" + start.substring(8)
                                                    : start.substring(5,7) +"/" + start.substring(8));

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime( startDate );
                                    for (int j = 1; j < difference; j++) {
                                        calendar.add( Calendar.DAY_OF_MONTH, 1);
                                        String format = simpleDateFormat.format(calendar.getTime());
                                        categories.add( format.substring(5,7).startsWith("0")?
                                                format.substring(6,7) + "/" + format.substring(8)
                                                : format.substring(5,7) + "/" + format.substring(8));
                                    }

                                    int sum_average_speed = 0;// 总秒数
                                    double max_average_speed = 0.0;
                                    int interruptNumber = 0;
                                    int sum_second = 0;
                                    int counts = 0 ;
                                    for (SportDataAddId sportDataAddId : list) {
                                        String time = sportDataAddId.getTime();
                                        String s1 = time.substring(5,7).startsWith("0")?
                                                time.substring(6,7) + "/" + time.substring(8,10)
                                                : time.substring(5,7) + "/" + time.substring(8,10);

                                        int i1 = categories.indexOf(s1);
                                        data[ i1 ] += sportDataAddId.getDuration() ;
                                        data2[ i1 ] += sportDataAddId.getCounts() ;

                                        sum_average_speed += sportDataAddId.getAverage_speed() ;
                                        sum_second += sportDataAddId.getDuration();
                                        max_average_speed = sportDataAddId.getAverage_speed() > max_average_speed ? sportDataAddId.getAverage_speed():max_average_speed;
                                        interruptNumber += sportDataAddId.getInterrupt_count();
                                        counts += sportDataAddId.getCounts() ;
                                    }

                                    // 显示图表
                                    // 时长
                                    drawLineChartQuanShu( data ,categories.toArray( new String[categories.size()]), "次", new String[]{"#64BB5C"});
                                    aaChartView01.aa_drawChartWithChartModel( aaChartModel );
                                    // 圈数
                                    drawLineChartQuanShu( data2, categories.toArray( new String[categories.size()]), "圈", new String[]{"#46B1E3"});
                                    aaChartView02.aa_drawChartWithChartModel( aaChartModel );

                                    // 运动数据显示
                                    // 平均速度
                                    DirectionalLayout parent = (DirectionalLayout) minText.getComponentParent();
                                    Text text1 = (Text) parent.getComponentAt(1);
                                    Text text2 = (Text) parent.getComponentAt(3);

                                    DirectionalLayout parent1 = (DirectionalLayout) interruptNumberText.getComponentParent();
                                    Text text3 = (Text) parent1.getComponentAt(1);

                                    DirectionalLayout parent2 = (DirectionalLayout) maxSpeedText.getComponentParent();
                                    Text text4 = (Text) parent2.getComponentAt(1);

                                    DirectionalLayout parent3 = (DirectionalLayout) averageSpeedText.getComponentParent();
                                    Text text5 = (Text) parent3.getComponentAt(1);
                                    if ( list.size() <= 0 ){
                                        averageSpeedText.setText("--");
                                        maxSpeedText.setText("--");
                                        interruptNumberText.setText("--");
                                        roundNumberText.setText("--");
                                        minText.setText("-");
                                        secondText.setText("-");

                                        // 下右
                                        text1.setText("");
                                        text2.setText("");

                                        // 下左
                                        text3.setText("");

                                        // 上右
                                        text4.setText("");

                                        // 上左
                                        text5.setText("");
                                        return ;
                                    }
                                    // 下右
                                    text1.setText("分");
                                    text2.setText("秒");
                                    // 下左
                                    text3.setText("次");
                                    // 上右
                                    text4.setText("圈/分钟");
                                    // 上左
                                    text5.setText("圈/分钟");

                                    int average_speed = sum_average_speed / list.size() ;
                                    averageSpeedText.setText(  average_speed + "");

                                    // 最大速度
                                    maxSpeedText.setText( (int)max_average_speed + "");

                                    // 绊绳数
                                    interruptNumberText.setText( interruptNumber + "");

                                    // 转动圈数
                                    roundNumberText.setText( counts + "" );

                                    // 运动时长（分，秒）
                                    minText.setText( sum_second / 60 + "");
                                    secondText.setText( sum_second % 60 + "");

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
        });
    }

    /**
     * end为空
     * @param start
     */
    private void appData_count( String start ) {
        this.getGlobalTaskDispatcher( TaskPriority.DEFAULT).asyncDispatch(()->{
            OkHttpUtils
                    .get()
                    .addParams("device_id", device_id)
                    .addParams("device_type", device_type)
                    .addParams("start",start)
                    .addHeader("time", new Date().getTime() +"" )
                    .addHeader("token", Constans.TOKEN)
                    .url( Constans.HTTP_HEAD + "/appData/count")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            System.out.println("数据统计失败");
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            ResultVO<CountSportData> resultVO = new Gson().fromJson(s, new TypeToken<ResultVO<CountSportData>>(){}.getType());
                            System.out.println("resultVO = " + resultVO);

                            if ( resultVO == null ){
                                ToastUtil.showToast(DataAbilitySlice.this, "统计数据为null");
                                return ;
                            }

                            // 画圈数
                            Integer[] data01 = new Integer[25];
                            Integer[] data02 = new Integer[25];
                            for (int j = 0; j < data01.length; j++) {
                                data01[j] = new Integer(0);
                                data02[j] = new Integer(0);
                            }
                            List<SportDataAddId> list = resultVO.getData().getList();
                            List<Count> countList = resultVO.getData().getCount();
                            int sum_average_speed = 0;// 总秒数
                            double max_average_speed = 0.0;
                            int interruptNumber = 0;
                            int sum_second = 0;

                            for (SportDataAddId sportDataAddId : list) {
                                // 运动开始时间
                                String time = sportDataAddId.getTime();
                                String hour = time.split(" ")[1].split(":")[0];

                                int recordTime = Integer.parseInt(hour) + 1;
                                data01[ recordTime ] =  data01[ recordTime ] + sportDataAddId.getCounts() ;
                                data02[ recordTime ] =  data02[ recordTime ] + sportDataAddId.getDuration() ;

                                sum_average_speed += sportDataAddId.getAverage_speed() ;
                                sum_second += sportDataAddId.getDuration();
                                max_average_speed = sportDataAddId.getAverage_speed() > max_average_speed ? sportDataAddId.getAverage_speed():max_average_speed;
                                interruptNumber += sportDataAddId.getInterrupt_count();
                            }
                            // 显示图表
                            // 运动时长画图
                            drawLineChartQuanShu(data02,categoriesQuanShu, "次", new String[]{"#64BB5C"});
                            aaChartView01.aa_drawChartWithChartModel(aaChartModel);
                            // 圈数画图
                            drawLineChartQuanShu(data01,categoriesQuanShu, "圈",new String[]{"#46B1E3"});
                            /*图表视图对象调用图表模型对象,绘制最终图形*/
                            aaChartView02.aa_drawChartWithChartModel(aaChartModel);

                            // 运动数据显示
                            // 平均速度
                            DirectionalLayout parent = (DirectionalLayout) minText.getComponentParent();
                            Text text1 = (Text) parent.getComponentAt(1);
                            Text text2 = (Text) parent.getComponentAt(3);

                            DirectionalLayout parent1 = (DirectionalLayout) interruptNumberText.getComponentParent();
                            Text text3 = (Text) parent1.getComponentAt(1);

                            DirectionalLayout parent2 = (DirectionalLayout) maxSpeedText.getComponentParent();
                            Text text4 = (Text) parent2.getComponentAt(1);

                            DirectionalLayout parent3 = (DirectionalLayout) averageSpeedText.getComponentParent();
                            Text text5 = (Text) parent3.getComponentAt(1);
                            if ( list.size() <= 0 ){
                                averageSpeedText.setText("--");
                                maxSpeedText.setText("--");
                                interruptNumberText.setText("--");
                                roundNumberText.setText("--");
                                minText.setText("-");
                                secondText.setText("-");

                                // 下右
                                text1.setText("");
                                text2.setText("");

                                // 下左
                                text3.setText("");

                                // 上右
                                text4.setText("");

                                // 上左
                                text5.setText("");
                                return ;
                            }
                            // 下右
                            text1.setText("分");
                            text2.setText("秒");
                            // 下左
                            text3.setText("次");
                            // 上右
                            text4.setText("圈/分钟");
                            // 上左
                            text5.setText("圈/分钟");

                            int average_speed = sum_average_speed / list.size() ;
                            averageSpeedText.setText(  average_speed + "");

                            // 最大速度
                            maxSpeedText.setText( (int)max_average_speed + "");

                            // 绊绳数
                            interruptNumberText.setText( interruptNumber + "");

                            // 转动圈数
                            roundNumberText.setText( countList.get(0).getCounts() + "" );

                            // 运动时长（分，秒）
                            minText.setText( sum_second / 60 + "");
                            secondText.setText( sum_second % 60 + "");
                        }
                    });
        });

    }


    /**
     * 画折线图
     */
    private void drawLineChartQuanShu( Object[] data, String[] categories,String unit, String[] colorsTheme ){
        aaChartModel = new AAChartModel()
                .chartType(AAChartType.Spline)
                .yAxisGridLineWidth(1.0f)
                .xAxisLabelsEnabled(true)
                .zoomType("xy")
                .markerSymbol("circle")
                .colorsTheme( colorsTheme )
                .legendEnabled( false )
                .markerRadius(3.0f)
                .tooltipValueSuffix(unit)
//                .tooltipEnabled(false)
                .categories( categories )
                .title("")
                .yAxisTitle("")
                .subtitle("")
                .backgroundColor("#FFFFFF")
//                .dataLabelsEnabled(true)
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                        .data( data )
                });
    }



    @Override
    public void onClick(Component component) {
        if ( this.dataBackBtn != null && component == this.dataBackBtn ){
            this.onBackPressed();
        }else if ( this.showCalendarImage != null && component == this.showCalendarImage ){
            showCalendarDialog( this );
        }else if ( this.cancelCalendarBtn != null && component == this.cancelCalendarBtn ){
            this.calendarCommonDialog.hide();
            this.year = dateText.getText().split("年")[0];
            this.month = dateText.getText().split("年")[1].split("月")[0];
            this.day = dateText.getText().split("年")[1].split("月")[1].substring(0,2);
        }else if ( this.okCalendarBtn != null && component == this.okCalendarBtn ){
            // todo: 确定日期的点击事件
            showThisDay();
        }else if ( this.todayBtn != null && component == this.todayBtn ){
            this.year = LocalDate.now().getYear() + "";
            this.month = LocalDate.now().getMonthValue() + "";
            this.month = this.month.length() > 1 ? this.month : "0" + this.month ;
            this.day = LocalDate.now().getDayOfMonth() + "";
            calendarDateText.setText( this.year + "年" + this.month + "月");
            addCalendar( calendarView,
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(),
                    LocalDate.now().getDayOfMonth());
        }else if ( this.lastImage != null && component == this.lastImage ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.MONTH, -1);

                String format = simpleDateFormat.format(calendar.getTime());
                this.year = format.split("-")[0] ;
                this.month = format.split("-")[1] ;
                this.day = format.split("-")[2] ;
                calendarDateText.setText( year + "年" + month + "月");

                getUITaskDispatcher().asyncDispatch(()->{
                    addCalendar( calendarView, Integer.parseInt(this.year),
                            Integer.parseInt(this.month),
                            Integer.parseInt(this.day) );
                });

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if ( this.nextImage != null && component == this.nextImage ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.MONTH, 1);

                String format = simpleDateFormat.format(calendar.getTime());
                this.year = format.split("-")[0] ;
                this.month = format.split("-")[1] ;
                this.day = format.split("-")[2] ;
                calendarDateText.setText( year + "年" + month + "月");

                getUITaskDispatcher().asyncDispatch(()->{
                    addCalendar( calendarView, Integer.parseInt(this.year),
                            Integer.parseInt(this.month),
                            Integer.parseInt(this.day) );
                });

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if ( component.getId() == dateDay.getId() ){
            currentTimeMode = 1 ;
            dayOrWeekOrMonthClick( dateDay, dateWeek, dateMonth );

            appData_count( year + "-" + month + "-" + day) ;
            dateText.setText(year + "年" + month + "月" + day + "日");

            String str = "当天数据(" + (LocalTime.now().getHour() > 12 ?
                    "下午" + (LocalTime.now().getHour()-12) + ":" + (LocalTime.now().getMinute()>=10?LocalTime.now().getMinute(): "0" + LocalTime.now().getMinute() ) + ")"
                    :"上午" + LocalTime.now().getHour()+ ":" + (LocalTime.now().getMinute()>=10?LocalTime.now().getMinute(): "0" + LocalTime.now().getMinute() ) + ")") ;
            showDataText.setText( str );

            showCalendarImage.setVisibility( Component.VISIBLE );

        }else if ( component.getId() == dateWeek.getId() ){
            currentTimeMode = 2 ;
            dayOrWeekOrMonthClick( dateWeek, dateDay, dateMonth );
            showDataText.setText( "近7天数据" );

            String startDate = LocalDate.now().getYear() + "-"
                    + LocalDate.now().getMonthValue() + "-"
                    + LocalDate.now().getDayOfMonth() ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                String format = simpleDateFormat.format(calendar.getTime());
                String year = format.split("-")[0] ;
                String month = format.split("-")[1] ;
                String day = format.split("-")[2] ;

                // 七天前的时间
                calendar.add( Calendar.DAY_OF_MONTH, -6);
                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( month.equals( _month ) ){
                    // 如果在同一月份
                    dateText.setText(year + "年" + month + "月" + _day + "日至"
                            + day + "日");
                }else {

                    if ( !year.equals( _year )){
                        // 不同年份
                        dateText.setText( _year + "年" + _month + "月" + _day + "日至"
                                + year + "年" + month + "月" + day + "日");
                    }else {
                        // 不是同月份
                        dateText.setText( _year + "年" + _month + "月" + _day + "日至"
                                + month + "月" + day + "日");
                    }

                }

                appData_count( _year + "-" + _month + "-" + _day, year + "-" + month + "-" + day);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            showCalendarImage.setVisibility( Component.HIDE );

        }else if ( component.getId() == dateMonth.getId() ){
            currentTimeMode = 3 ;
            dayOrWeekOrMonthClick( dateMonth, dateDay, dateWeek );
            showDataText.setText( "近30天数据" );

            String startDate = LocalDate.now().getYear() + "-"
                    + LocalDate.now().getMonthValue() + "-"
                    + LocalDate.now().getDayOfMonth() ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                String format = simpleDateFormat.format(calendar.getTime());
                String year = format.split("-")[0] ;
                String month = format.split("-")[1] ;
                String day = format.split("-")[2] ;

                // 一个月前的日期
                calendar.add( Calendar.MONTH, -1 );
                calendar.add( Calendar.DAY_OF_MONTH, 1 );
                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( !year.equals( _year )){
                    // 不同年份
                    dateText.setText( _year + "年" + _month + "月" + _day + "日至"
                            + year + "年" + month + "月" + day + "日");
                }else {
                    // 不是同月份
                    dateText.setText(_year + "年" + _month + "月" + _day + "日至"
                            + month  + "月" + day + "日");
                }

                appData_count( _year +"-" + _month + "-" + _day,
                        year + "-" + month + "-" + day);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            showCalendarImage.setVisibility( Component.HIDE );
        }else if ( component.getId() == last_day_week_month.getId() ){

            if( currentTimeMode == 1 ){
                last_day_week_monthClick( year, month, day );
            }else if( currentTimeMode == 2 || currentTimeMode == 3 ){
                String date = dateText.getText();
                String endDate = date.split("至")[0] ;
                String _year = endDate.substring(0,4);
                String _month = endDate.substring(5,7);
                String _day = endDate.substring(8,10);

                last_day_week_monthClick( _year, _month, _day );
            }
        }else if ( component.getId() == next_day_week_month.getId() ){

            if( currentTimeMode == 1 ){
                next_day_week_monthClick( year, month, day );
            }else if( currentTimeMode == 2 || currentTimeMode == 3 ){
                String date = dateText.getText();
                String _year = date.split("年").length > 2 ?
                        date.split("至")[1].split("年")[0] :
                        date.substring(0,4);

                int length = date.split("月").length ;
                String _month = "";
                if (length > 2) {
                    String s = date.split("至")[1].split("月")[0];
                    _month = s.substring( s.length()-2, s.length());
                }else {
                    _month = date.substring(5,7);
                }

                String _day = date.substring(date.length()-3,date.length()-1);

                next_day_week_monthClick( _year, _month, _day );
            }
        }
    }


    /**
     * 右箭头选择时间段
     * @param year
     * @param month
     * @param day
     */
    private void next_day_week_monthClick(String year, String month, String day) {
        if( currentTimeMode == 1 ){

            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, 1);

                String format = simpleDateFormat.format(calendar.getTime());
                this.year = format.split("-")[0] ;
                this.month = format.split("-")[1] ;
                this.day = format.split("-")[2] ;

                dateText.setText( this.year + "年" + this.month + "月" + this.day + "日");

                appData_count( this.year + "-" + this.month + "-" + this.day );
                showDataText.setText( this.year + "年" + this.month + "月" + this.day + "日" + "数据");

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else if( currentTimeMode == 2 ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, 1);
                String format = simpleDateFormat.format(calendar.getTime());
                year = format.split("-")[0] ;
                month = format.split("-")[1] ;
                day = format.split("-")[2] ;

                calendar.add( Calendar.DAY_OF_MONTH, 6);

                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( month.equals( _month ) ){
                    // 如果在同一月份
                    dateText.setText( _year + "年" + _month + "月" + day + "日至"
                            + _day + "日");
                }else {

                    if (!year.equals(_year)) {
                        // 不同年份
                        dateText.setText(year + "年" + month + "月" + day + "日至"
                                +_year + "年" + _month + "月" + _day + "日" );
                    } else {
                        // 不是同月份
                        dateText.setText(_year + "年" + month + "月" + day + "日至"
                                + _month + "月" + _day + "日");
                    }
                }
                appData_count( _year + "-" + _month + "-" + day, year+ "-" + month  +"-" + _day);

            }catch (ParseException e) {
                e.printStackTrace();
            }
        }else if( currentTimeMode == 3 ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, 1);
                String format = simpleDateFormat.format(calendar.getTime());
                year = format.split("-")[0] ;
                month = format.split("-")[1] ;
                day = format.split("-")[2] ;

                calendar.add( Calendar.MONTH, 1);
                calendar.add( Calendar.DAY_OF_MONTH, -1);

                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( !year.equals( _year )){
                    // 不同年份
                    dateText.setText(year + "年" + month + "月" + day + "日至"
                            + _year + "年" + _month + "月" + _day + "日");
                }else {
                    // 不是同月份
                    dateText.setText( year + "年" + month + "月" + day + "日至"
                            + _month  + "月" + _day + "日");
                }

                appData_count( year+"-" + month + "-" + day ,
                        _year+"-" + _month + "-" + _day );

            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 左箭头选择时间段
     * @param year
     * @param month
     * @param day
     */
    private void last_day_week_monthClick(String year, String month, String day) {
        if( currentTimeMode == 1 ){

            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, -1);

                String format = simpleDateFormat.format(calendar.getTime());
                this.year = format.split("-")[0] ;
                this.month = format.split("-")[1] ;
                this.day = format.split("-")[2] ;

                dateText.setText( this.year + "年" + this.month + "月" + this.day + "日");

                showDataText.setText( this.year + "年" + this.month + "月" + this.day + "日" + "数据");
                appData_count( this.year + "-" + this.month + "-" + this.day );

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else if( currentTimeMode == 2 ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, -1);
                String format = simpleDateFormat.format(calendar.getTime());
                year = format.split("-")[0] ;
                month = format.split("-")[1] ;
                day = format.split("-")[2] ;

                calendar.add( Calendar.DAY_OF_MONTH, -6);

                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( month.equals( _month ) ){
                    // 如果在同一月份
                    dateText.setText( _year + "年" + _month + "月" + _day + "日至" + day + "日");
                }else {

                    if (!year.equals(_year)) {
                        // 不同年份
                        dateText.setText(_year + "年" + _month + "月" + _day + "日至"
                                + year + "年" + month + "月" + day + "日");
                    } else {
                        // 不是同月份
                        dateText.setText(_year + "年" + _month + "月" + _day + "日至"
                                + month + "月" + day + "日");
                    }
                }

                appData_count( _year + "-" + _month + "-" + _day, year+ "-" + month  +"-" + day);

            }catch (ParseException e) {
                e.printStackTrace();
            }
        }else if( currentTimeMode == 3 ){
            String startDate = year + "-" + month + "-" + day ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = simpleDateFormat.parse(startDate);
                calendar.setTime( date );
                calendar.add( Calendar.DAY_OF_MONTH, -1);
                String format = simpleDateFormat.format(calendar.getTime());
                year = format.split("-")[0] ;
                month = format.split("-")[1] ;
                day = format.split("-")[2] ;

                calendar.add( Calendar.MONTH, -1);
                calendar.add( Calendar.DAY_OF_MONTH, 1);
                format = simpleDateFormat.format(calendar.getTime());
                String _year = format.split("-")[0] ;
                String _month = format.split("-")[1] ;
                String _day = format.split("-")[2] ;

                if ( !year.equals( _year )){
                    // 不同年份
                    dateText.setText( _year + "年" + _month + "月" + _day + "日至"
                            + year + "年" + month + "月" + day + "日");
                }else {
                    // 不是同月份
                    dateText.setText( _year + "年" + _month + "月" + _day + "日至"
                            + month  + "月" + day + "日");
                }

                appData_count( _year + "-" + _month + "-" + _day,
                        year + "-" + month + "-" + day);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 日、周、月的点击事件
     * @param dateDay   表示被点击的按钮
     * @param dateWeek
     * @param dateMonth
     */
    private void dayOrWeekOrMonthClick(DirectionalLayout dateDay, DirectionalLayout dateWeek, DirectionalLayout dateMonth) {
        Text text01 = (Text) dateDay.getComponentAt(0);
        text01.setTextColor( new Color( Color.getIntColor("#0A59F7")));
        dateDay.getComponentAt(1).setVisibility( Component.VISIBLE );

        Text text02 = (Text) dateWeek.getComponentAt(0);
        text02.setTextColor( Color.BLACK  );
        dateWeek.getComponentAt(1).setVisibility( Component.HIDE );

        Text text03 = (Text) dateMonth.getComponentAt(0);
        text03.setTextColor( Color.BLACK  );
        dateMonth.getComponentAt(1).setVisibility( Component.HIDE );
    }


    /**
     * 确定日期后，渲染界面数据
     */
    private void showThisDay() {
        // 隐藏弹框
        calendarCommonDialog.hide();
        // 显示选择的日期
        dateText.setText( year + "年" + month + "月" + day + "日" );
        // 相关折线图的显示
        appData_count( year + "-" + month + "-" + day );
    }


    /**
     * 日历弹框
     */
    private void showCalendarDialog(Context context) {
        //1、定义对话弹框
        calendarCommonDialog = new CommonDialog(context);
        //设置弹框的大小
        calendarCommonDialog.setSize(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        //设置对齐方式
        calendarCommonDialog.setAlignment(LayoutAlignment.BOTTOM);

        //2、加载xml文件到内存中
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_calendar_common, null, false);

        cancelCalendarBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_cancel_calendar);
        okCalendarBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_ok_calendar);
        todayBtn = (Button) dl.findComponentById(ResourceTable.Id_btn_today);
        lastImage = (Image) dl.findComponentById(ResourceTable.Id_img_last_month);
        nextImage = (Image) dl.findComponentById(ResourceTable.Id_img_next_month);

        cancelCalendarBtn.setClickedListener(this);
        okCalendarBtn.setClickedListener(this);
        todayBtn.setClickedListener(this);
        lastImage.setClickedListener(this);
        nextImage.setClickedListener(this);

        calendarView = (TableLayout) dl.findComponentById(ResourceTable.Id_calendarView);
        // 添加日历到calendarView容器中
        addCalendar( calendarView,
                Integer.parseInt( year ),
                Integer.parseInt( month ),
                Integer.parseInt( day ));

        calendarDateText = (Text) dl.findComponentById(ResourceTable.Id_text_calendar_showDate);
        calendarDateText.setText( year + "年" + month + "月");

        //将XML文件中的布局交给吐司弹框
        calendarCommonDialog.setContentCustomComponent(dl);
        calendarCommonDialog.setAutoClosable( false );
        //弹框显示时间
        calendarCommonDialog.setDuration(999999999);
        //设置对话框圆角的半径
        calendarCommonDialog.setCornerRadius(50.0f);
        //显示弹框
        calendarCommonDialog.show();
    }


    /**
     * 根据日月年生成日历
     * @param calendarView
     * @param year
     * @param month
     * @param day
     */
    private void addCalendar(TableLayout calendarView, int year, int month, int day) {
        calendarView.removeAllComponents();
        // 星期
        final String[] days = new String[]{"日","一","二","三","四","五","六"};

        // 循环星期
        for (String s : days) {
            // 调用text并设置值
            Text text = text();
            text.setText(s);
            text.setTextSize(50);
            // 追加到table布局中
            calendarView.addComponent(text);
        }

        // 当月的天数
        int Count= new Date(year, month, 0).getDate();
        // 当月的第一天的星期
        int mothday = new Date(year, month-1, 1).getDay() -1 ;
        // 是否为星期日
        if( mothday >= 0) {
            // 循环空格
            for (int i = 0; i < mothday; i++) {
                // 调用text并设置值
                Text text = text();
                text.setText("");
                // 追加到table布局中
                calendarView.addComponent(text);
            }
        }else if ( mothday < 0 ){
            // 循环空格
            for (int i = 0; i < 6; i++) {
                // 调用text并设置值
                Text text = text();
                text.setText("");
                // 追加到table布局中
                calendarView.addComponent(text);
            }
        }

        // 循环天数
        for (int i = 1; i <= Count; i++) {
            // 调用text并设置值
            Text text = text();
            if(day == i) {
                text.setTextColor(Color.WHITE);
                ShapeElement shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_date_selected);
                text.setBackground(  shapeElement );
            }
            text.setText("" + i);

            DataAbilitySlice _this = this;
            text.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    _this.day = text.getText();

                    addCalendar(calendarView, year, month, Integer.parseInt( text.getText() )  );
                }
            });
            // 追加到table布局中
            calendarView.addComponent(text);
        }

    }

    private Text text() {
        Text text = new Text(getContext());
        text.setWidth(145);
        text.setHeight(145);
        text.setTextSize(40);
        text.setTextColor(Color.BLACK);
        ShapeElement shapeElement = new ShapeElement(this, ResourceTable.Graphic_background_ability_date_unSelected);
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

}
