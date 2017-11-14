(function() {
  /** Module */
  var dashboard = angular.module('app.dashboard', [])
  dashboard.$inject = ['$location'];
  /** Controller */
  dashboard.controller('dashboardController', [
    '$scope', 'dashboardService', '$rootScope', '$interval', '$timeout',
    function($scope, dashboardService, $rootScope, $interval, $timeout) {
      var vm = this;
      $rootScope.change_flag = false; // 不切换
      $scope.update_day_date = new Date();
      $scope.update_date = new Date();

      $timeout(function() {
        $("#menu-scroll").slick({
          slidesToShow: 5,
          slidesToScroll: 1,
          autoplay: true,
          autoplaySpeed: 30000,
          vertical: true,
          verticalSwiping: true
        });
        $scope.current_tab = 1;
        $('#menu-scroll').on('beforeChange', function(event, slick, currentSlide, nextSlide) {
          $scope.current_tab = nextSlide + 1;
          if ($scope.current_tab == 5) { // 天气

            $timeout(function() {
              $(".weather-scroll").slick({
                slidesToShow: 2,
                slidesToScroll: 1,
                cssEase: 'linear',
                autoplay: true,
                centerMode: true,
                autoplaySpeed: 0,
                speed: 7000,
                vertical: true,
                verticalSwiping: true
              });
            }, 1500)
          }
        });


      }, 1000)

      // 中间系统切换事件
      $scope.toggle = function(num) {
        $scope.current_tab = num;
        //$interval.cancel(stop);
        $('#menu-scroll').slick('slickGoTo', num - 1);
      }

      // 获取信息资源目录和共享情况统计数
      var getDataCount = function() {
        // 获取已汇集数据量及最新一周新增
        dashboardService.getDataCountLast().then(function(result) {
          $scope.dataCountLast = result.data.body;
        })

        // 获取其他数据
        dashboardService.getResourceCombing().then(function(result) {
          $scope.ResourceCombing = result.data.body[0];
        })

        // 部门已接入数据总量
        dashboardService.getAccessDataNum().then(function(result) {
          $scope.deptAccessData = result.data.body;
          $scope.deptAccessMax = _.max(_.map($scope.deptAccessData, 'dataNum')) + 30;
          _.forEach($scope.deptAccessData, function(item) {
              item.i_width = item.dataNum / $scope.deptAccessMax * 100 + '%';
            })
          $timeout(function() {
            $(".dept-access-rank").slick({
                  slidesToShow: 8,
                  slidesToScroll: 1,
                  autoplay: true,
                  autoplaySpeed: 2500,
                  vertical: true,
                  verticalSwiping: true
                });
          }, 10);
          

        })
      }

      // 中间系统车载前置
      var getCarSystem = function() {
        // 交易情况
        dashboardService.getVehicle().then(function(result) {
          var data = result.data.body[0];
          if($scope.Vehicle && $scope.Vehicle.yesterday && (!data.yesterday || data.yesterday == '' || data.yesterday == 0)) {
            data.yesterday = angular.copy($scope.Vehicle.yesterday);
          }
          $scope.Vehicle = data;
        })
      }

      function CivilServantGenerator(){
        $scope.CivilServant = {};
        var all = Number(sessionStorage.getItem("all"));
        var careerPersonnel = Number(sessionStorage.getItem("careerPersonnel"));
        var onTheJob = Number(sessionStorage.getItem("onTheJob"));
        var beLate = Number(sessionStorage.getItem("beLate"));
        var leaveEarly = Number(sessionStorage.getItem("leaveEarly"));
        var daily = Number(sessionStorage.getItem("daily"));
        var weekly = Number(sessionStorage.getItem("weekly"));
        var quarter = Number(sessionStorage.getItem("quarter"));
        if(!all)  {
          $scope.CivilServant.all = Math.round(Math.random()*(3141-3042)+3042);
          sessionStorage.setItem("all",$scope.CivilServant.all);
        }
        else{
          $scope.CivilServant.all = all;
        }
        if(!careerPersonnel)  {
          $scope.CivilServant.careerPersonnel = 1000;
          sessionStorage.setItem("careerPersonnel",$scope.CivilServant.careerPersonnel);
        }
        else{
          $scope.CivilServant.careerPersonnel = careerPersonnel;
        }
        if(!onTheJob)  {
          $scope.CivilServant.onTheJob = 0+$scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);
          sessionStorage.setItem("onTheJob",$scope.CivilServant.onTheJob);
        }
        else{
          $scope.CivilServant.onTheJob = onTheJob;
        }
        if(!beLate)  {
          $scope.CivilServant.beLate = Math.round(Math.random()*(50-30)+30); // 前一天迟到
          sessionStorage.setItem("beLate",$scope.CivilServant.beLate);
        }
        else{
          $scope.CivilServant.beLate = beLate;
        }
        if(!leaveEarly)  {
          $scope.CivilServant.leaveEarly = Math.round(Math.random()*(50-30)+30); // 前一天早退
          sessionStorage.setItem("leaveEarly",$scope.CivilServant.leaveEarly);
        }
        else{
          $scope.CivilServant.leaveEarly = leaveEarly;
        }
        if(!daily)  {
          $scope.CivilServant.daily = 0+$scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 前一天提交工作日志
          sessionStorage.setItem("daily",$scope.CivilServant.daily);
        }
        else{
          $scope.CivilServant.daily = daily;
        }
        if(!weekly)  {
          $scope.CivilServant.weekly = 0+$scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 最近一周提交工作周报
          sessionStorage.setItem("weekly",$scope.CivilServant.weekly);
        }
        else{
          $scope.CivilServant.weekly = weekly;
        }
        if(!quarter)  {
          $scope.CivilServant.quarter = 0+$scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(700-500)+500);// 最近一季度考核成绩优秀
          sessionStorage.setItem("quarter",$scope.CivilServant.quarter);
        }
        else{
          $scope.CivilServant.quarter = quarter;
        }


        //$scope.CivilServant.all = Math.round(Math.random()*(3141-3042)+3042);// 全市公务员注册人数
        // $scope.CivilServant.careerPersonnel = 1000;// 全市事业单位注册人数
        // $scope.CivilServant.onTheJob = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 前一天在岗
        // $scope.CivilServant.beLate = Math.round(Math.random()*(50-30)+30); // 前一天迟到
        // $scope.CivilServant.leaveEarly = Math.round(Math.random()*(50-30)+30); // 前一天早退
        // $scope.CivilServant.daily = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 前一天提交工作日志
        // $scope.CivilServant.weekly = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 最近一周提交工作周报
        // $scope.CivilServant.quarter = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(700-500)+500);// 最近一季度考核成绩优秀

      }

      // 中间系统公务员考核系统
      var getCivilServant = function() {
        dashboardService.getCivilServant().then(function(result) {
          //$scope.CivilServant = result.data.body[0]; 
          // 以下操作单纯为了临时演示，按给的逻辑造的假数据，需要切换时再使用实时数据
          CivilServantGenerator();
        })
      }
      // 临时增加定时器模拟公务员系统的假数据，真实环境需删除
      $interval(function() { // 按天更新
         $scope.CivilServant.onTheJob = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 前一天在岗
         sessionStorage.setItem("onTheJob",$scope.CivilServant.onTheJob);
         $scope.CivilServant.beLate = Math.round(Math.random()*(50-30)+30); // 前一天迟到
         sessionStorage.setItem("beLate",$scope.CivilServant.beLate);
         $scope.CivilServant.leaveEarly = Math.round(Math.random()*(50-30)+30); // 前一天早退
         sessionStorage.setItem("leaveEarly",$scope.CivilServant.leaveEarly);
         $scope.CivilServant.daily = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 前一天提交工作日志
         sessionStorage.setItem("daily",$scope.CivilServant.daily);
      }, 86400000);
      $interval(function() { // 按周更新
        $scope.CivilServant.weekly = $scope.CivilServant.all+$scope.CivilServant.careerPersonnel-Math.round(Math.random()*(50-30)+30);// 最近一周提交工作周报
        sessionStorage.setItem("weekly",$scope.CivilServant.weekly);
      }, 604800000);

      // 中间系统政务审批服务
      var getGovernmentApprovalSystem = function() {
        dashboardService.getGovernmentApprovalSystem().then(function(result) {
          $scope.GovernmentApprovalSystem = result.data.body[0];
        })
      }

      // 区域自动站监测平台
      var getAreaObservation = function() {
        // 监测站个数
        dashboardService.getMeteorologicalObservationStation().then(function(result) {
          $scope.MeteorologicalObservationStation = result.data.body[0];
        })

        // 最高温度
        dashboardService.getMos_maxTemperature().then(function(result) {
          var data = result.data.body[0];
          if($scope.Mos_maxTemperature && $scope.Mos_maxTemperature.zgqw && (!data.zgqw || data.zgqw =='')) {
            data.zgqw = angular.copy($scope.Mos_maxTemperature.zgqw);
          }
          $scope.Mos_maxTemperature = data;
        })

        // 最低温度
        dashboardService.getMos_minTemperature().then(function(result) {
          var data = result.data.body[0];
          if($scope.Mos_minTemperature && $scope.Mos_minTemperature.zdqw && (!data.zdqw || data.zdqw =='')) {
            data.zdqw = angular.copy($scope.Mos_minTemperature.zdqw);
          }
          $scope.Mos_minTemperature = data;
        })

        // 最大降水
        dashboardService.getMos_maxRainfall().then(function(result) {
          var data = result.data.body[0];
          if($scope.Mos_maxRainfall && $scope.Mos_maxRainfall.yxsyl && (!data.yxsyl || data.yxsyl =='')) {
            data.yxsyl = angular.copy($scope.Mos_maxRainfall.yxsyl);
          }
          $scope.Mos_maxRainfall = data;
        })


      }

      // 公共气象服务平台
      var weatherForecast = function() {
        // 短时天气预报
        $scope.hide_weather = false;
        dashboardService.getWeatherForecast().then(function(result) {
          if (result && result.data && result.data.body && result.data.body[0]) {
            $scope.hide_weather = false;
            $scope.WeatherForecast = result.data.body[0];
          } else {
            $scope.hide_weather = true;
          }

        })

        // 最新灾害性天气预警
        $scope.hide_disaster = false;
        dashboardService.getDisasterWarning().then(function(result) {
          $scope.DisasterWarning = result.data.body;
          if ($scope.DisasterWarning && $scope.DisasterWarning.length > 0) {
            // $timeout(function() {
            //   $(".weather-scroll").slick({
            //     slidesToShow: 2,
            //     slidesToScroll: 1,
            //     cssEase: 'linear',
            //     autoplay: true,
            //     centerMode: true,
            //     autoplaySpeed: 0,
            //     speed: 7000,
            //     vertical: true,
            //     verticalSwiping: true
            //   });
            // }, 300)
          } else {
            $scope.hide_disaster = true;
          }

        })
      }

      // 手动刷新灾难性天气数据
      $scope.getTimingData = function() {
        dashboardService.getDisasterWarning().then(function(result) {
          $scope.DisasterWarning = result.data.body;
          if ($scope.DisasterWarning && $scope.DisasterWarning.length > 0) {
            $scope.hide_disaster = false;
            $timeout(function() {
              $(".weather-scroll").slick({
                slidesToShow: 2,
                slidesToScroll: 1,
                cssEase: 'linear',
                autoplay: true,
                centerMode: true,
                autoplaySpeed: 0,
                speed: 7000,
                vertical: true,
                verticalSwiping: true
              });
            }, 300)
          } else {
            $scope.hide_disaster = true;
          }

        })
      }

      // 手动刷新天气预报
      $scope.getWeatherData = function() {
        dashboardService.getWeatherForecast().then(function(result) {
          if (result && result.data && result.data.body && result.data.body[0]) {
            $scope.hide_weather = false;
            $scope.WeatherForecast = result.data.body[0];
          } else {
            $scope.hide_weather = true;
          }

        })
      }

      // 崇州市信用网
      var getCreditWeb = function() {
        // 全市市场主体
        dashboardService.getCreditWebOne().then(function(result) {
          $scope.CreditWebOne = result.data.body[0];
        })

        // 最近一月
        dashboardService.getCreditWebTwo().then(function(result) {
          $scope.CreditWebTwo = result.data.body[0];
        })

        // 最后一项
        dashboardService.getCreditWebThree().then(function(result) {
          $scope.CreditWebThree = result.data.body[0];
        })

        $scope.entries = [{
            label: '社保登记',
            color: "#01ccd1"
          }, {
            label: '税务登记'
          }, {
            label: '司法行政登记',
            color: "#01ccd1"
          }, {
            label: '事业单位登记'
          }, {
            label: '企业注册人'
          }, {
            label: '市场主体退出（注销）'
          }, {
            label: '市场主体退出（吊销）',
            color: "#01ccd1"
          }, {
            label: '事业单位年报'
          }, {
            label: '机关群团登记'
          }, {
            label: '诚信企业评价',
            color: "#01ccd1"
          }, {
            label: '批评谴责',
            color: "#01ccd1"
          }, {
            label: '失信被执行人'
          }, {
            label: '水电欠费信息'
          }, {
            label: '司法执行'
          }, {
            label: '司法判决'
          }, {
            label: '产品质量监督抽查'
          }, {
            label: '拖欠公积金'
          }, {
            label: '表彰奖励'
          }, {
            label: '中小企业信用等级'
          }, {
            label: '食品生产经营企业监管等级',
            color: "#01ccd1"
          }, {
            label: '纳税信用评级'
          }, {
            label: '其它行政处理措施',
            color: "#01ccd1"
          }, {
            label: '重大税收违法'
          }, {
            label: '行政奖励'
          }, {
            label: '行政强制措施'
          }, {
            label: '工商局行政处罚',
            color: "#01ccd1"
          }, {
            label: '行政处罚'
          }, {
            label: '民政登记',
            color: "#01ccd1"
          }, {
            label: '商标'
          }, {
            label: '专利'
          }, {
            label: '动产抵押'
          }, {
            label: '对外贸易经营者备案登记',
            color: "#01ccd1"
          }, {
            label: '绿色食品'
          }, {
            label: '农产品地理标志'
          }, {
            label: '无公害农产品'
          }, {
            label: '行业资质资格',
            color: "#01ccd1"
          }, {
            label: '行政许可',
            color: "#01ccd1"
          }, {
            label: '市场主体退出',
            color: "#01ccd1"
          }, {
            label: '股权出质'
          }, {
            label: '消费者投诉站'
          }, {
            label: '协会成员单位',
            color: "#01ccd1"
          }, {
            label: '机构代码证发放'
          }, {
            label: '公积金缴存登记'
          }, {
            label: '市场主体'
          }


        ];

        var settings = {

          entries: $scope.entries,
          width: 900,
          height: 400,
          radius: '40%',
          radiusMin: 175,
          bgDraw: true,
          bgColor: 'transparent',
          opacityOver: 1.00,
          opacityOut: 0.05,
          opacitySpeed: 10,
          fov: 1000,
          speed: 0.2,
          fontFamily: 'Oswald, Arial, sans-serif',
          fontSize: '18',
          fontColor: '#fff',
          fontWeight: 'normal', //bold
          fontStyle: 'normal', //italic 
          fontStretch: 'normal', //wider, narrower, ultra-condensed, extra-condensed, condensed, semi-condensed, semi-expanded, expanded, extra-expanded, ultra-expanded
          fontToUpperCase: true

        };

        //var svg3DTagCloud = new SVG3DTagCloud( document.getElementById( 'holder'  ), settings );
        //$( '#tag-cloud' ).svg3DTagCloud( settings );
        $timeout(function() {
          var o = {
            textFont: 'Arial, Helvetica, sans-serif',
            maxSpeed: 0.05,
            minSpeed: 0.01,
            textColour: null,
            textHeight: 12,
            outlineMethod: 'colour',
            fadeIn: 800,
            outlineColour: '#41b1c3',
            outlineOffset: 0,
            depth: 0.97,
            minBrightness: 0.2,
            wheelZoom: false,
            reverse: true,
            shadowBlur: 2,
            shuffleTags: true,
            shadowOffset: [1, 1],
            stretchX: 1.6,
            initial: [0, 0.1],
            clickToFront: 600,
            maxSpeed: 0.01,
            outlineDashSpeed:0.5
          };
          var s = (new Date).getTime() / 360;
          o.initial[0] = 0.18 * Math.cos(s);
          o.initial[1] = 0.18* Math.sin(s);
          if (!$('#myCanvas').tagcanvas(o, 'tags')) {
            // something went wrong, hide the canvas container
            $('#myCanvasContainer').hide();
          }
        }, 600);
      }

      // 大数据产业政策
      var getIndustrialPolicy = function() {
        dashboardService.getIndustrialPolicy().then(function(result) {
          $scope.IndustrialPolicy = result.data.body;
          _.forEach($scope.IndustrialPolicy, function(item, index) {
              if (index % 3 == 0) {
                item.itemStyle = 'item-up';
              } else if (index % 3 == 1) {
                item.itemStyle = 'item-center';
              } else {
                item.itemStyle = 'item-down';
              }
            })
            // 崇州市大数据产业政策
          $timeout(function() {
            $("#pro-list").slick({
              slidesToShow: 3,
              slidesToScroll: 1,
              autoplay: true,
              autoplaySpeed: 3000,
              vertical: true,
              verticalSwiping: true
            });
          }, 500)
        })
      }

      var init = function() {
        // get deptartment data count
        dashboardService.getDeptDataQuantity().then(function(result) {
          var data = result.data.body[0];
          if (data) {
            vm.deptData = data;
          }
        })

        //获取数据图谱政务数据总类及已采集数据量
        dashboardService.getDataCount().then(function(result) {
          var data = result.data.body[0];
          if (data) {
            vm.deptDataCount = data;
          }
        })

        getDataCount();
        getCarSystem();
        getCivilServant();
        getGovernmentApprovalSystem();
        getAreaObservation();
        weatherForecast();
        getCreditWeb();
        getIndustrialPolicy();
      }

      init();

      // 信息资源目录和共享情况内容切换
      $scope.toggleDept = function(){
        if($scope.allow_toggle) {// 鼠标在视图内，禁止自动切换
         
          var temp = angular.copy($scope.toggleMap);
          $scope.toggleMap = (temp+1)%3;
          if($scope.toggleMap == 1) {
            _.forEach($scope.deptAccessData, function(item) {
            item.i_width = '0%';
          })
          $timeout(function() {
            _.forEach($scope.deptAccessData, function(item) {
              item.i_width = item.dataNum / $scope.deptAccessMax * 100 + '%';
            })
            $('.dept-access-rank').slick('slickGoTo', 0);
          }, 100)
          }
          
        
        }
        
      }

      // 大数据产业综合指数与IPC切换
      $scope.toggleBigdata = function(){
        if($scope.allow_ipc_toggle) {// 鼠标在视图内，禁止自动切换
          if ($scope.toggleIPC) {
          $scope.toggleIPC = false;
        } else {

          $scope.toggleIPC = true;
        }
        }
        
      }

      $scope.deptToggleClick = function(){// 部门视图切换
        $scope.allow_toggle = true;
        $scope.toggleDept();
      }

      $scope.bigdataToggleClick = function(){// bigdata视图切换
        $scope.allow_ipc_toggle = true;
        $scope.toggleBigdata();
      }

      $scope.toggleMap = 0;
      $scope.toggleIPC = true;
      $scope.deptViewTitle = "切换视图";
      $interval(function() {
        $scope.toggleDept();
        $scope.toggleBigdata();
      }, 30000)

      //部门视图鼠标进入
      $scope.allow_toggle = true;
      $scope.mouseEnter = function(){
        console.log('mouse enter....');
        $scope.allow_toggle = false;
      }
      //部门视图鼠标移除
      $scope.mouseLeave = function(){
        console.log('mouse leave....');
        $scope.allow_toggle = true;

      }

      //bigdata视图鼠标进入
      $scope.allow_ipc_toggle = true;
      $scope.mouseIPCEnter = function(){
        $scope.allow_ipc_toggle = false;

      }
      //部门视图鼠标移除
      $scope.mouseIPCLeave = function(){
        $scope.allow_ipc_toggle = true;
      }

      //定时刷新数据
      $interval(function() { // 按天更新的数据
        
        $scope.update_day_date = new Date();
      }, 86400000);

      $interval(function() { // 按15分钟更新的数据
        weatherForecast(); // 气象
      }, 900000);

      $interval(function() { // 按1小时更新的数据
        getAreaObservation(); // 区域检测
        getCarSystem(); // 车载
        getCivilServant(); //公务员系统
        getGovernmentApprovalSystem(); // 政务系统
        getCreditWeb(); // 信用网
        getDataCount(); // 信息资源目录和共享情况
        getIndustrialPolicy(); //大数据产业政策
        $scope.update_date = new Date();
      }, 3600000);

      $scope.changeRoute = function() {
        dashboardService.getStatus().then(function(result) {
          var data = result.data;
          var newValue = "";
          if (data == 1) {
            newValue = "";
          } else {
            newValue = 1;
          }
          dashboardService.setStatus(newValue).then(function() {

          })
        })
      }
    }
  ]);

  dashboard.run(['$rootScope', '$interval', '$location', 'dashboardService', function($rootScope, $interval, $location, dashboardService) {
    $rootScope.state = ''; // 全屏，不动态切换
    var timer = $interval(function() {
      var current_url = $location.path();
      dashboardService.getStatus().then(function(result) {
        var data = result.data;
        if (data == 1) { // 切换
          if (current_url.indexOf('bigdata') > -1) {
            $location.path('/idcuse')
          }
          if (current_url.indexOf('support') > -1) {
            $location.path('/datamap')
          }
          if (current_url.indexOf('datamap') > -1) {
            $location.path('/support')
          }
          if (current_url.indexOf('idcuse') > -1) {
            $location.path('/bigdata')
          }
        }
      })

    }, 5000)
  }]);

  /** Service */
  dashboard.factory('dashboardService', ['$http', 'URL',
    function($http, URL) {
      return {
        getDeptDataQuantity: getDeptDataQuantity,
        getStatus: getStatus,
        getIdcUse: getIdcUse,
        getIdcUseTime: getIdcUseTime,
        setStatus: setStatus,
        getDataInfoNum: getDataInfoNum,
        getDataCountLast: getDataCountLast,
        getResourceCombing: getResourceCombing,
        getDataCount: getDataCount,
        getAccessDataNum: getAccessDataNum,
        getDeptCoords: getDeptCoords,
        getDeptContract:getDeptContract,//崇信签约或者意向签约的数据资源
        getVehicle: getVehicle, // 车载前置系统数据
        getVehicleView: getVehicleView, //  车载前置管理系统折线图
        getCivilServant: getCivilServant, // 公务员考核系统
        getGovernmentApprovalSystem: getGovernmentApprovalSystem, //政务审批服务平台
        getMeteorologicalObservationStation: getMeteorologicalObservationStation, //区域自动监测平台监测站个数
        getMos_maxTemperature: getMos_maxTemperature, // 监测站最高温度
        getMos_minTemperature: getMos_minTemperature, //最低温度
        getMos_maxRainfall: getMos_maxRainfall, //最大降水
        getWeatherForecast: getWeatherForecast, // 短时天气预报
        getDisasterWarning: getDisasterWarning, //灾害性天气预警  
        getCreditWebOne: getCreditWebOne, // 崇州市信用网市场主体
        getCreditWebTwo: getCreditWebTwo, //崇州市信用网最近一月
        getCreditWebThree: getCreditWebThree, // 崇州市信用网最后一项
        getIndustrialPolicy: getIndustrialPolicy //大数据产业政策
      }

      function getDeptDataQuantity() {
        return $http.get(
          URL + '/depDataInfo/quantity'
        )
      }

      function getStatus() {
        return $http.get(
          URL + '/status/get'
        )
      }

      function getIdcUse() {
        return $http.get(
          URL + '/serverMonitor/detail'
        )
      }

      function getIdcUseTime() {
        return $http.get(
          URL + '/serverMonitor/detailTime'
        )
      }

      function setStatus(data) {
        return $http.put(
          URL + '/status/set?value=' + data
        )
      }

      function getDataInfoNum() {
        return $http.get(
          URL + '/depDataInfo/dataInfoNum'
        )
      }

      // 政务信息资源目录和共享情况统计
      function getDataCountLast() {
        return $http.get(
          URL + '/depDataInfo/dataCountLast'
        )
      }

      function getResourceCombing() {
        return $http.get(
          URL + '/combing/ResourceCombing'
        )
      }

      // datamap -> 政务数据总类及已采集数据量
      function getDataCount() {
        return $http.get(
          URL + '/depDataInfo/dataCount'
        )
      }

      // dept -> 部门已接入数据总量top8
      function getAccessDataNum() {
        return $http.get(
          URL + '/depDataInfo/accessDataNum'
        )
      }

      // 获取部门坐标系
      function getDeptCoords() {
        return $http.get(
          'assets/file/depts_coord.json'
        )
      }

      function getDeptContract(){
        return $http.get(
          'assets/file/dept_contract.json'
        )
      }

      function getVehicle() {
        return $http.get(
          URL + '/sixApp/vehicle'
        )
      }

      function getVehicleView() {
        return $http.get(
          URL + '/sixApp/vehicleView'
        )
      }

      function getCivilServant() {
        return $http.get(
          URL + '/sixApp/civilServant'
        )
      }

      function getGovernmentApprovalSystem() {
        return $http.get(
          URL + '/sixApp/governmentApprovalSystem'
        )
      }

      function getMeteorologicalObservationStation() {
        return $http.get(
          URL + '/sixApp/meteorologicalObservationStation'
        )
      }

      function getMos_maxTemperature() {
        return $http.get(
          URL + '/sixApp/mos_maxTemperature'
        )
      }

      function getMos_minTemperature() {
        return $http.get(
          URL + '/sixApp/mos_minTemperature'
        )
      }


      function getMos_maxRainfall() {
        return $http.get(
          URL + '/sixApp/mos_maxRainfall'
        )
      }

      function getWeatherForecast() {
        return $http.get(
          URL + '/sixApp/weatherForecast'
        )
      }

      function getDisasterWarning() {
        return $http.get(
          URL + '/sixApp/disasterWarning'
        )
      }

      function getCreditWebOne() {
        return $http.get(
          URL + '/sixApp/creditWebOne'
        )
      }

      function getCreditWebTwo() {
        return $http.get(
          URL + '/sixApp/creditWebTwo'
        )
      }

      function getCreditWebThree() {
        return $http.get(
          URL + '/sixApp/creditWebThree'
        )
      }

      function getIndustrialPolicy() {
        return $http.get(
          URL + '/depDataInfo/industrialPolicy'
        )
      }
    }
  ]);

  dashboard.service('dashboard.chartDockProgress', ['$http', 'URL',
    function($http, URL) {
      // if (URL) {
      //   return $http({
      //     method: 'GET',
      //     url: URL + '/',
      //     withCredentials: true
      //   });
      // } else {
      //   console.error('API Not Found in config.js');
      // }
    }
  ]);

  dashboard.service('dashboard.chartDockDataset', ['$http', 'URL',
    function($http, URL) {
      // if (URL) {
      //   return $http({
      //     method: 'GET',
      //     url: URL + '/',
      //     withCredentials: true
      //   });
      // } else {
      //   console.error('API Not Found in config.js');
      // }
    }
  ]);

  dashboard.service('dashboard.chartDeptDataTable', ['$http', 'URL',
    function($http, URL) {
      if (URL) {
        return $http({
          method: 'GET',
          url: URL + '/depDataInfo/table',
          withCredentials: true
        });
      } else {
        console.error('API Not Found in config.js');
      }
    }
  ]);

  dashboard.service('dashboard.chartDeptDataColumn', ['$http', 'URL',
    function($http, URL) {
      if (URL) {
        return $http({
          method: 'GET',
          url: URL + '/depDataInfo/column',
          withCredentials: true
        });
      } else {
        console.error('API Not Found in config.js');
      }
    }
  ]);


  /** Directive */
  dashboard.directive('wiservDockProgress', ['dashboard.chartDockProgress',
    function(appSupportService) {
      return {
        restrict: 'ACE',
        template: "<div id='dockProgress'></div>",
        link: function(scope, element, attrs) {
          // lawsChart.then(function(response){
          //   var data = response.data.body;
          // })
          var screen_width = screen.width;
          var base_level = 4;
          if (screen_width < 2000) {
            base_level = 4;
          }
          if (screen_width < 1600) {
            base_level = 3;
          }
          var option = {
            title: {
              text: '对接进度',
              left: 'center',
              top: 15,
              textStyle: {
                color: 'rgb(0,225,252)',
                fontSize: 8 * base_level,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            radar: [{
              indicator: [{
                text: '市民一卡通系统',
                max: 70
              }, {
                text: '气象观测系统',
                max: 70,
                axisLabel: false
              }, {
                text: '成都市公共气象服务平台',
                max: 70,
                axisLabel: false
              }, {
                text: '公务员管理系统',
                max: 70,
                axisLabel: false
              }, {
                text: '综合执法智慧服务平台',
                max: 70,
                axisLabel: false
              }, {
                text: '成都市企业信用系统 ',
                max: 70,
                axisLabel: false
              }, {
                text: '成都崇州行政审批系统 ',
                max: 70,
                axisLabel: false
              }, {
                text: '数字化城管系统 ',
                max: 70,
                axisLabel: false
              }, {
                text: '省行权平台 ',
                max: 70,
                axisLabel: false
              }],
              center: ['52%', '55%'],
              radius: base_level * 50,
              splitNumber: 7,
              name: {
                formatter: '{value}',
                textStyle: {
                  color: 'rgba(220, 220, 220, 1)',
                  fontSize: 4 * base_level
                }
              },
              splitArea: {
                areaStyle: {
                  color: ['transparent'],
                  shadowColor: 'rgba(0, 0, 0, 0.3)',
                  shadowBlur: 10
                }
              },
              axisLine: {
                show: false,
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.5)'
                }
              },
              splitLine: {
                lineStyle: {
                  color: 'rgba(204, 204, 204, 0.3)'
                }
              },
              axisLabel: {
                show: true,
                formatter: '{value}%',
                textStyle: {
                  color: '#DDD'
                }
              }
            }],
            series: [{
              name: '雷达图',
              type: 'radar',
              tooltip: {
                trigger: 'item'
              },
              itemStyle: {
                emphasis: {
                  // color: 各异,
                  lineStyle: {
                    width: 4
                  }
                }
              },
              data: [{
                value: [60, 30, 60, 10, 50, 20, 68, 50, 10],
                name: '对接进度',
                symbolSize: 5,
                lineStyle: {
                  normal: {
                    color: 'rgb(69,128,180)',
                    width: 2,
                    shadowColor: 'rgb(94,154,208)',
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowBlur: 12
                  }
                },
                itemStyle: {
                  normal: {
                    color: 'rgb(69,128,180)'
                  }
                }
              }]
            }]
          }
          var chartProgress = echarts.init((element.find('#dockProgress'))[0]);
          //chartProgress.setOption(option);

          setTimeout(function() {
            var box_width = element.find('#dockProgress')[0].clientWidth;
            $('#dockProgress').css({
              'width': box_width
            });
            chartProgress.clear();
            chartProgress.resize();
            chartProgress.setOption(option);
          }, 1000);

        }
      }
    }
  ]);

  dashboard.directive('wiservDockDataset', ['dashboard.chartDockDataset',
    function(dockDatasetService) {
      return {
        restrict: 'ACE',
        template: "<div id='dockDataset'></div>",
        link: function(scope, element, attrs) {
          // lawsChart.then(function(response){
          //   var data = response.data.body;
          // })
          var screen_width = screen.width;
          var base_level = 4;
          if (screen_width < 2000) {
            base_level = 4;
          }
          if (screen_width < 1600) {
            base_level = 3;
          }
          var option = {
            title: {
              text: '对接后采集到平台的数据项',
              left: 'center',
              top: 48,
              textStyle: {
                color: 'rgb(0,225,252)',
                fontSize: 8 * base_level,
                fontWeight: 'normal'
              }
            },
            color: ['#3398DB'],
            tooltip: {
              trigger: 'axis',
              axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
              }
            },
            grid: {
              top: '24%',
              left: '3%',
              right: '4%',
              bottom: '20%',
              containLabel: true
            },
            xAxis: [{
              type: 'category',
              data: ['市民一卡通系统', '省行权平台', '数字化城管系统', '成都崇州行政审批系统', '成都市企业信用系统', '综合执法智慧服务平台', '公务员管理系统', '成都市公共气象服务平台', '气象观测系统'],
              axisTick: {
                alignWithLabel: true
              },
              axisLabel: {
                rotate: 45,
                interval: 0,
                textStyle: {
                  color: 'rgba(220, 220, 220, 1)',
                  fontSize: 4 * base_level
                }
              },
              axisLine: {
                lineStyle: {
                  color: 'rgba(204, 204, 204, 0.5)'
                }
              }
            }],
            yAxis: [{
              type: 'value',
              axisTick: {
                show: false
              },
              axisLine: {
                show: false
              },
              axisLabel: {
                textStyle: {
                  color: 'rgba(220, 220, 220, 1)',
                  fontSize: 3 * base_level
                }
              },
              splitLine: {
                lineStyle: {
                  color: ['rgba(204, 204, 204, 0.5)']
                }
              }
            }],
            series: [{
              name: '采集数据项',
              type: 'bar',
              barWidth: '45%',
              label: {
                normal: {
                  show: true,
                  position: 'top',
                  textStyle: {
                    color: 'rgba(240, 240, 240, 1)',
                    fontSize: 3 * base_level
                  }
                }
              },
              itemStyle: {
                normal: {
                  color: 'rgb(0,112,192)',
                  shadowColor: 'rgb(8, 152, 255)',
                  shadowBlur: 8
                }
              },
              data: [16, 5, 15, 4, 16, 28, 18, 37, 24]
            }]
          };

          var charDataset = echarts.init((element.find('#dockDataset'))[0]);
          //charDataset.setOption(option);

          setTimeout(function() {
            var box_width = element.find('#dockDataset')[0].clientWidth;
            $('#dockDataset').css({
              'width': box_width
            });
            charDataset.clear();
            charDataset.resize();
            charDataset.setOption(option);
          }, 2000);


          // 定时器：动态滑过效果
          // var i = 0;
          // setInterval(function() {
          //   if (i == 9) {
          //     i = 0;
          //   }
          //   charDataset.dispatchAction({
          //     type: 'showTip',
          //     seriesIndex: 1,
          //     dataIndex: i
          //   });
          //   i++;
          // }, 2500);



          // charDataset.on('mouseover',function(params) {
          //   console.log(params);
          // })
        }
      }
    }
  ]);


  dashboard.directive('wiservChartMigrate', ['dashboardService',
    function(dashboardService) {
      return {
        restrict: 'ACE',
        template: "<div id='deptData' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          var chartInstance = echarts.init((element.find('#deptData'))[0]);
          dashboardService.getDeptCoords().then(function(result) {
              var geoCoordMap = result.data.coords;
              var data = result.data.data;


              function formtGCData(geoData, data, srcNam, dest) {
                var tGeoDt = [];
                if (dest) {
                  for (var i = 0, len = data.length; i < len; i++) {
                    if (srcNam != data[i].name) {
                      tGeoDt.push({
                        coords: [geoData[srcNam], geoData[data[i].name]]
                      });
                    }
                  }
                } else {
                  for (var i = 0, len = data.length; i < len; i++) {
                    if (srcNam != data[i].name) {
                      if (data[i].name == "成都市政务中心" || data[i].name == "成都市气象局" || data[i].name == "成都市工商局") {

                      } else if (data[i].name == '成都市政府部门') {
                        tGeoDt.push({
                          coords: [geoData[data[i].name], geoData[srcNam]],
                          lineStyle: {
                            normal: {
                              color: 'rgb(103,254,0)'
                            }
                          }
                        });
                      } else {
                        tGeoDt.push({
                          coords: [geoData[data[i].name], geoData[srcNam]]
                        });
                      }

                    }
                  }
                }
                return tGeoDt;
              }

              function formtVData(geoData, data, srcNam) {
                var tGeoDt = [];
                for (var i = 0, len = data.length; i < len; i++) {
                  var tNam = data[i].name
                  if (srcNam != tNam) {
                    if (tNam == "成都市政府部门") {
                      tGeoDt.push({
                        name: tNam,
                        value: geoData[tNam],
                        symbolSize: 16,
                        itemStyle: {
                          normal: {
                            color: 'rgb(103,254,0)'
                          }
                        },
                        label: {
                          normal: {
                            //position:'top',
                            position: [-150, -60],
                            textStyle: {
                              color: '#FFF',
                              fontSize: 24,
                              fontWeight: 100
                            }
                          }
                        }
                      });
                    } else if (tNam == "成都市政务中心" || tNam == "成都市气象局" || tNam == "成都市工商局") {
                      tGeoDt.push({
                        name: tNam,
                        value: geoData[tNam],
                        symbolSize: 0.001,
                        label: {
                          normal: {
                            position: cd_pos
                          }
                        }
                      });
                    } else {
                      if (geoData[tNam][0] > 91 && geoData[tNam][0] < 119 && geoData[tNam][1] < 21) {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 6,
                          label: {
                            normal: {
                              position: [-30, 22]
                            }
                          },
                          itemStyle: {
                            normal: {
                              color: 'rgb(255,241,81)'
                            }
                          }
                        });
                      } else if (geoData[tNam][0] <= 91) {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 6,
                          label: {
                            normal: {
                              position: [-80, -5]
                            }
                          },
                          itemStyle: {
                            normal: {
                              color: 'rgb(238,141,9)'
                            }
                          }
                        });
                      } else {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 6

                        });
                      }
                    }

                  }

                }
                tGeoDt.push({
                  name: srcNam,
                  value: geoData[srcNam],
                  symbolSize: 16,
                  label: {
                    normal: {
                      position: [-100, -60],
                      textStyle: {
                        color: '#FFF',
                        fontSize: 24,
                        fontWeight: 100
                      }
                    }
                  },
                  itemStyle: {
                    normal: {
                      color: 'rgb(255,234,1)'
                    }
                  }
                });
                return tGeoDt;
              }

              var planePath = 'circle';

              var zoom = 0.8;
              var layoutCenter = ['38%', '45%'];
              var fontSize = 15;
              var cd_pos = [60, -5]; // 成都市政府部门子集position
              var screen_width = screen.width;
              var screen_height = screen.height;

              if (screen_width < 1600) {
                zoom = 0.6;
                layoutCenter = ['29%', '35%'];
                fontSize = 14;
                cd_pos = [15, -8];
              }

              var option = {
                backgroundColor: 'transparent',
                title: {
                  text: '',
                  left: '5',
                  top: '10px',
                  itemStyle: {
                    normal: {
                      borderColor: 'rgba(100,149,237,1)',
                      borderWidth: 0.5,
                      areaStyle: {
                        color: '#1b1b1b'
                      }
                    }
                  }
                },
                tooltip: {
                  trigger: 'item',
                },
                geo: {
                  map: 'china',
                  show: false,
                  zoom: zoom,
                  layoutCenter: layoutCenter,
                  layoutSize: '100%',
                  left: '2%',
                  label: {
                    emphasis: {
                      show: false
                    }
                  },
                  roam: true,
                  silent: true,
                  itemStyle: {
                    normal: {
                      areaColor: 'transparent',
                      borderColor: '#000'
                    },
                    emphasis: {
                      areaColor: '#2a333d'
                    }
                  }
                },
                series: [{

                  type: 'lines',
                  zlevel: 2,
                  silent: true,
                  effect: {
                    show: true,
                    period: 6,
                    trailLength: 0.005,
                    color: 'rgba(255,255,255,.8)',
                    symbol: planePath,
                    symbolSize: 4
                  },
                  lineStyle: {
                    normal: {
                      color: '#a6c84c',
                      width: 1,
                      opacity: 0.4,
                      curveness: 0.2,
                      shadowColor: 'rgb(255,241,81)',
                      shadowBlur: 10
                    }
                  },
                  data: formtGCData(geoCoordMap, data, '崇州市数据共享交换平台', false)
                }, {

                  type: 'effectScatter',
                  coordinateSystem: 'geo',
                  zlevel: 2,
                  silent: true,
                  rippleEffect: {
                    period: 4,
                    scale: 3,
                    brushType: 'stroke'
                  },
                  label: {
                    normal: {
                      show: true,
                      position: [24, -5],
                      formatter: '{b}',
                      textStyle: {
                        //color: 'rgb(2,255,29)',
                        fontSize: fontSize,
                        fontFamily: 'yahei',
                        fontWeight: 100
                      }
                    }
                  },
                  symbolSize: 4,
                  itemStyle: {
                    normal: {
                      color: 'rgb(2,255,29)',
                      shadowBlur: 35,
                      shadowColor: 'rgba(2,255,29,1)',
                      opacity: 0.6
                    }
                  },

                  data: formtVData(geoCoordMap, data, '崇州市数据共享交换平台')
                }]
              };

              chartInstance.setOption(option);
            })
            // var geoCoordMap = {
            //   '长春': [129.8154, 50.2584],
            //   '长沙': [135.0823, 25.2568],
            //   '崇州市数据共享交换平台': [108.6992, 32.7682],
            //   '西安': [134.1162, 34.2004],
            //   '深圳': [124.5435, 22.5439],
            //   '济南': [127.1582, 36.8701],
            //   '海口': [110.3893, 16.8516],
            //   '沈阳': [142.1238, 22.1216],
            //   '武汉': [134.3896, 30.6628],
            //   '昆明': [102.9199, 18.4663],
            //   '杭州': [139.5313, 29.8773],
            //   '成都市政府部门': [89.9526, 54.7617],
            //   '成都市政务中心': [74.1526, 55.7617],
            //   '成都市气象局': [74.1526, 54.5617],
            //   '成都市工商局': [74.1526, 53.3617],
            //   '拉萨': [81.1865, 30.1465],
            //   '天津': [117.4219, 49.4189],
            //   '合肥': [137.29, 32.0581],
            //   '呼和浩特': [107.4124, 50.4901],
            //   '哈尔滨': [129.9688, 40.368],
            //   '北京': [110.4551, 54.2539],
            //   '南京': [134.8062, 41.9208],
            //   '南宁': [96.479, 18.1152],
            //   '南昌': [116.0046, 19.6633],
            //   '乌鲁木齐': [87.9236, 46.5883],
            //   '上海': [135.4648, 31.2891]
            // };



        }
      }
    }
  ]);

 dashboard.directive('wiservChartContract', ['dashboardService',
    function(dashboardService) {
      return {
        restrict: 'ACE',
        template: "<div id='deptData' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          var chartInstance = echarts.init((element.find('#deptData'))[0]);
          dashboardService.getDeptContract().then(function(result) {
              var geoCoordMap = result.data.coords;
              var data = result.data.data;


              function formtGCData(geoData, data, srcNam, dest) {
                var tGeoDt = [];
                if (dest) {
                  for (var i = 0, len = data.length; i < len; i++) {
                    if (srcNam != data[i].name) {
                      tGeoDt.push({
                        coords: [geoData[srcNam], geoData[data[i].name]]
                      });
                    }
                  }
                } else {
                  for (var i = 0, len = data.length; i < len; i++) {
                    if (srcNam != data[i].name) {
                      if(data[i].name == "成都医保数据" || data[i].name == "舆情数据" || data[i].name == "行政审批数据") {
                        if(data[i].name == "行政审批数据"){
                          tGeoDt.push({
                          coords: [geoData[srcNam], geoData[data[i].name]],
                          lineStyle: {
                            normal: {
                              curveness: 0.18,
                            }
                          }
                        });
                          
                        }
                        else{
                          tGeoDt.push({
                          coords: [geoData[srcNam], geoData[data[i].name]],
                        });
                        }
                        
                      }
                      else{
                        tGeoDt.push({
                          coords: [geoData[data[i].name], geoData[srcNam]]
                        });
                      }
                      

                    }
                  }
                }
                return tGeoDt;
              }

              function formtVData(geoData, data, srcNam) {
                var tGeoDt = [];
                for (var i = 0, len = data.length; i < len; i++) {
                  var tNam = data[i].name
                  if (srcNam != tNam) {
                    if (tNam == '国家资源卫星数据') {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbol:'circle',
                          symbolSize: 14,
                          label: {
                            normal: {
                              position: [-90, -90]
                            }
                          }
                        });
                      } else if (tNam == '成都工商数据') {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 14,
                          label: {
                            normal: {
                              align:'right',
                              position: [-50, -10],
                              rich: {
                                  b: {
                                    align:'right',
                                  },
                                  c:{
                                    align:'right',
                                  }
                              }
                            }
                          }
                        });
                      } else if(tNam == '社会信用数据') {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 14,
                          label: {
                            normal: {
                              align:'right',
                              position: [-10, 25]
                            }
                          }
                        });
                      } else if(tNam == '行政审批数据'){
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 14,
                          label: {
                            normal: {
                              position: [-18, 25]
                            }
                          }
                        });
                      }else if(tNam == '知识产权数据'){
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 14,
                          label: {
                            normal: {
                              position: [30, -60]
                            }
                          }
                        });
                      }
                      else {
                        tGeoDt.push({
                          name: tNam,
                          value: geoData[tNam],
                          symbolSize: 14,
                          label: {
                            normal: {
                              position: [30, -30]
                            }
                          }
                        });
                      }

                  }

                }
                tGeoDt.push({
                  name: srcNam,
                  value: geoData[srcNam],
                  symbol:'image://../assets/images/sign.png',
                  symbolSize: 26,
                  label: {
                    normal: {
                      position: [40, -60],
                      rich:{
                        a:{
                          color:'#edfc02',
                          fontSize:20
                        }
                      }
                    }
                  },
                  itemStyle: {
                    normal: {
                      color: 'rgb(255,234,1)'
                    }
                  }
                });
                return tGeoDt;
              }

              var planePath = 'circle';

              var zoom = 0.8;
              var layoutCenter = ['38%', '45%'];
              var fontSize = 20;
              var cd_pos = [60, -5]; // 成都市政府部门子集position
              var screen_width = screen.width;
              var screen_height = screen.height;

              if (screen_width < 1600) {
                zoom = 0.6;
                layoutCenter = ['29%', '35%'];
                fontSize = 14;
                cd_pos = [15, -8];
              }

              var option = {
                backgroundColor: 'transparent',
                title: {
                  text: '',
                  left: '5',
                  top: '10px',
                  itemStyle: {
                    normal: {
                      borderColor: 'rgba(100,149,237,1)',
                      borderWidth: 0.5,
                      areaStyle: {
                        color: '#1b1b1b'
                      }
                    }
                  }
                },
                tooltip: {
                  trigger: 'item',
                },
                geo: {
                  map: 'china',
                  show: false,
                  zoom: zoom,
                  layoutCenter: layoutCenter,
                  layoutSize: '100%',
                  left: '2%',
                  label: {
                    emphasis: {
                      show: false
                    }
                  },
                  roam: true,
                  silent: true,
                  itemStyle: {
                    normal: {
                      areaColor: 'transparent',
                      borderColor: '#000'
                    },
                    emphasis: {
                      areaColor: '#2a333d'
                    }
                  }
                },
                series: [{

                  type: 'lines',
                  zlevel: 2,
                  silent: true,
                  effect: {
                    show: false,
                    period: 6,
                    trailLength: 0.005,
                    color: 'rgba(255,255,255,.8)',
                    symbol: planePath,
                    symbolSize: 4
                  },
                  lineStyle: {
                    normal: {
                      color: 'rgb(18,236,252)',
                      width: 2,
                      opacity: 1,
                      curveness: 0.35,
                      shadowColor: 'rgb(18,236,252)',
                      shadowBlur: 10
                    }
                  },
                  data: formtGCData(geoCoordMap, data, '崇州市数据共享交换平台', false)
                }, {

                  type: 'effectScatter',
                  coordinateSystem: 'geo',
                  zlevel: 2,
                  silent: true,
                  rippleEffect: {
                    period: 10,
                    scale: 3,
                    brushType: 'stroke'
                  },
                  label: {
                    normal: {
                      show: true,
                      position: [24, -5],
                      formatter: function(params){
                        var data_arra = params.name.split('\n');
                        if(data_arra.length>1) {
                          if(data_arra.length>2){
                            return '{a|' +data_arra[0]+'}\n{b|'+data_arra[1]+'}{c|' + data_arra[2]+'}';
                          }
                          return '{a|' +data_arra[0]+'}\n{b|'+data_arra[1]+'}';
                        }
                        return '{a|' +data_arra[0]+'}';
                      },
                       rich: {
                          a: {
                              color:'#3bbbf0',
                              fontSize:fontSize,
                              lineHeight: 40,
                          },
                          b: {
                            color:'#3bbbf0',
                            fontSize:fontSize,
                             fontFamily: 'digiFont',
                             verticalAlign:'bottom',
                             padding:[0,10,0,0]
                          },
                          c:{
                            color:'#3bbbf0',
                            fontSize:12,
                            verticalAlign:'bottom',
                          }
                      },
                      textStyle: {
                        color: '#3bbbf0',
                        fontSize: fontSize,
                        fontFamily: 'digiFont',
                        fontWeight: 100
                      }
                    }
                  },
                  symbolSize: 4,
                  itemStyle: {
                    normal: {
                      color: 'rgb(18,236,252)',
                      shadowBlur: 35,
                      shadowColor: 'rgba(2,255,29,1)',
                      opacity: 1
                    }
                  },

                  data: formtVData(geoCoordMap, data, '崇州市数据共享交换平台')
                }]
              };

              chartInstance.setOption(option);
            })


        }
      }
    }
  ]);

  // 部门已接入数据总量
  // dashboard.directive('wiservChartAccessData', ['dashboardService','$interval', function(dashboardService,$interval) {
  //   return {
  //     restrict: 'ACE',
  //     template: "<div id='accessData' style='width:100%;height:100%'></div>",
  //     link: function(scope, element, attrs) {
  //       var chartInstance = echarts.init((element.find('#accessData'))[0]);
  //       var stop_roll = null; // 部门排行滚动
  //       // 获取部门已接入数据总量Top8
  //       function draw(){
  //         dashboardService.getAccessDataNum().then(function(result) {
  //         var data = result.data.body;
  //         if (data) {
  //           var full_data = angular.copy(data);
  //           var other_data = _.remove(data, function(n,index) {
  //             return index>7;
  //           });

  //           var deptAccessDataTop8 = data;
  //           var depts = _.map(deptAccessDataTop8,'DEP_NAME');
  //           var values = _.map(deptAccessDataTop8,'dataNum');
  //           var max = _.max(_.map(full_data,'dataNum')) + 30;
  //           var max_datas = [];
  //           for(var i=0; i<values.length; i++) {
  //             max_datas.push(max);
  //           }

  //           var grid_right = '15%';
  //           var grid_bottom = '20%';
  //           var screen_width = screen.width;
  //           var screen_height = screen.height;

  //           if(screen_width < 1600) {
  //             grid_right = '30%';
  //             grid_bottom = '40%';
  //           }
  //           var option = {
  //             grid: {
  //               top: '1%',
  //               left: '3%',
  //               right: grid_right,
  //               bottom: grid_bottom,
  //               containLabel: true
  //             },
  //             xAxis: {
  //               type: 'value',
  //               boundaryGap: [0, 0],
  //               axisLine: {
  //                 show: false
  //               },
  //               axisLabel: {
  //                 show: false
  //               },
  //               axisTick: {
  //                 show: false
  //               },
  //               splitLine: {
  //                 show: false
  //               }
  //             },
  //             yAxis: {
  //               type: 'category',
  //               data: depts,
  //               inverse:true,
  //               axisLine: {
  //                 show: false
  //               },
  //               axisLabel: {
  //                 margin: 19,
  //                 textStyle: {
  //                   color: 'rgb(100,154,155)',
  //                   fontSize: 15
  //                 },
  //                 formatter:function(value,index){
  //                   var s_num = 0;
  //                   if(value && value.length>7) {
  //                     value = value.substring(0,7);
  //                     s_num = 7-value.length;
  //                   }

  //                   var val_str = value;
  //                   for(var i=0; i<s_num; i++) {
  //                       val_str += "   ";
  //                   }
  //                   return val_str;
  //                 }
  //               },
  //               axisTick: {
  //                 show: false
  //               },
  //               splitLine: {
  //                 show: false
  //               }
  //             },
  //             series: [{ // For shadow
  //               type: 'bar',
  //               barWidth: '50%',
  //               itemStyle: {
  //                 normal: {
  //                   color: 'rgba(31,54,74,1)'
  //                 }
  //               },
  //               barGap: '-80%',
  //               barCategoryGap: '100%',
  //               data: max_datas,
  //               animation: false
  //             }, {
  //               name: '部门接入数据量',
  //               barWidth: '30%',
  //               type: 'bar',
  //               zlevel: 1,
  //               data: values,
  //               label: {
  //                 normal: {
  //                   show: true,
  //                   position: 'right',
  //                   textStyle: {
  //                     color: 'rgb(100,154,155)',
  //                     fontSize: 15
  //                   }
  //                 }
  //               },
  //               itemStyle: {
  //                 normal: {
  //                   color: new echarts.graphic.LinearGradient(
  //                     0, 0, 1, 1, [{
  //                       offset: 0,
  //                       color: '#1e83d8'
  //                     }, {
  //                       offset: 0.5,
  //                       color: 'rgb(26,137,183)'
  //                     }, {
  //                       offset: 1,
  //                       color: 'rgb(14,181,217)'
  //                     }]
  //                   )
  //                 },
  //                 emphasis: {
  //                   color: new echarts.graphic.LinearGradient(
  //                     0, 0, 1, 1, [{
  //                       offset: 0,
  //                       color: '#188df0'
  //                     }, {
  //                       offset: 0.5,
  //                       color: 'rgb(26,137,183)'
  //                     }, {
  //                       offset: 1,
  //                       color: 'rgb(14,181,217)'
  //                     }]
  //                   )
  //                 }
  //               },
  //             }]
  //           };
  //           var  option_origin = angular.copy(option);
  //           chartInstance.clear();
  //           chartInstance.setOption(option);

  //           var current_index = 8;
  //           stop_roll = $interval(function(){
  //             if(current_index == 15) {
  //               current_index = 8;
  //               chartInstance.clear();
  //               chartInstance.setOption(option_origin);
  //               option = angular.copy(option_origin);
  //             }
  //             else{
  //               option.series[1].data.shift();
  //               option.series[1].data.push(full_data[current_index].dataNum);

  //               option.yAxis.data.shift();
  //               option.yAxis.data.push(full_data[current_index].DEP_NAME);
  //               current_index++;
  //               chartInstance.setOption(option);
  //             }

  //           },2500)


  //         }
  //       })
  //       }
  //       draw();

  //       $interval(function(){
  //         if(stop_roll) {
  //           $interval.cancel(stop_roll);
  //         }
  //         draw();
  //       },864000);

  //     }
  //   }
  // }])

  // 前一天消费趋势图
  dashboard.directive('wiservChartCar', ['dashboardService', '$interval',
    function(dashboardService, $interval) {
      return {
        restrict: 'ACE',
        template: "<div id='carData' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          var chartInstance = echarts.init((element.find('#carData'))[0]);
          var draw_flag = false; // 是否绘制过

          function draw() {
            dashboardService.getVehicleView().then(function(result) {
              var data = result.data.body;
              var x_values = _.map(data, "_id");
              var values = [];

              var x_data = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"];
              var null_flag = true;
              _.forEach(x_data,function(item,index){
                var obj = {};
                obj.name = item;
                var date_index = _.indexOf(x_values, item);
                if(date_index > -1) {
                  obj.value = data[date_index].count;
                  if(obj.value != 0) {
                    null_flag = false;
                  }
                }
                else{
                  obj.value = 0;
                }
                
                values.push(obj);
              })
              console.log(values);
              var option = {
                title: {
                  text: '前一天消费趋势图（笔）',
                  left: '3%',
                  textStyle: {
                    fontSize: 30,
                    fontWeight: 'normal',
                    color: 'rgb(237,252,2)'
                  }
                },
                tooltip: {
                  trigger: 'axis'
                },

                grid: {
                  left: '3%',
                  top: '20%',
                  bottom: '40%',
                  right: '35%'
                },

                xAxis: {
                  type: 'category',
                  boundaryGap: false,
                  axisTick: {
                    show: false
                  },
                  axisLine: {
                    lineStyle: {
                      color: 'rgb(8,88,123)'
                    }
                  },
                  axisLabel: {
                    textStyle: {
                      color: 'rgba(255,255,255,.6)',
                      fontSize: 22
                    }
                  },
                  data: x_data
                },
                yAxis: {
                  type: 'value',
                  boundaryGap: false,
                  axisLabel: {
                    margin: 15,
                    textStyle: {
                      color: 'rgba(255,255,255,.6)',
                      fontSize: 22
                    }
                  },
                  axisLine: {
                    lineStyle: {
                      color: 'rgb(8,88,123)'
                    }
                  },
                  splitLine: {
                    lineStyle: {
                      width: 2,
                      color: 'rgb(8,88,123)'
                    }
                  }
                },
                series: [{
                  name: '前一天消费(笔)',
                  type: 'line',
                  stack: '总量',
                  data: values,
                  symbolSize: 6,
                  itemStyle: {
                    normal: {
                      color: 'rgb(237,252,2)',
                      shadowColor: '#FFF',
                      shadowBlur: 6
                    }
                  },
                  lineStyle: {
                    normal: {
                      width: 3,
                      color: 'rgb(237,252,2)'
                    }
                  }
                }]
              };

              if(!draw_flag || !null_flag) {// 数据都为0 ，则不重绘，保持上次的数据
                draw_flag = true;
                chartInstance.clear();
                chartInstance.setOption(option);
              }
              
            })
          }

          draw();

          $interval(function() { // 每天刷新
            draw()
          }, 86400000);
        }
      }
    }
  ])

  dashboard.directive('wiservIdcUse', ['dashboardService', '$location',
    function(dashboardService, location) {
      return {
        restrict: 'ACE',
        template: "<div id='idcUse' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          var path = location.path();
          var plotly_width = 500;
          var plotly_height = 300;

          dashboardService.getIdcUse().then(function(response) {
            var rateData = response.data.body;
            scope.memRateList = _.map(rateData, 'memRate');
            scope.cpuRateList = _.map(rateData, 'cpuRate');
            scope.diskRateList = _.map(rateData, 'diskRate');
            scope.nodeName = _.map(rateData, 'nodeName');

            function unpack(rows, key) {
              return rows.map(function(row) {
                return row[key];
              });
            }

            function groupData(response) {
              var rateData = response.data.body;
              scope.memRateList = _.map(rateData, 'memRate');
              scope.cpuRateList = _.map(rateData, 'cpuRate');
              scope.diskRateList = _.map(rateData, 'diskRate');
              var rows = [];
              for (var i = 0; i < 2; i++) {
                var monitors = {};
                monitors[""] = i.toString();
                switch (i) {
                  case 0:
                    for (var j = 0; j < rateData.length; j++) {
                      monitors[j] = rateData[j].memRate.toString();
                    }
                    break;
                  case 1:
                    for (var j = 0; j < rateData.length; j++) {
                      monitors[j] = rateData[j].cpuRate.toString();
                    }
                    break;
                    // case 2:
                    //   for (var j = 0; j < rateData.length; j++) {
                    //     monitors[j] = rateData[j].diskRate.toString();
                    //   }
                    //   break;
                  default:
                    break;
                }
                rows.push(monitors);
              }

              var z_data = []
              for (var i = 0; i < 9; i++) {
                z_data.push(unpack(rows, i));
              }

              return z_data;
            }
            var z_group_data = groupData(response);
            var text = [];
            for (var i = 0; i < z_group_data.length; i++) {
              var obj = [];
              for (var j = 0; j < z_group_data[i].length; j++) {
                // var data =  scope.nodeName[i] + '<br>硬盘使用率: ' + z_group_data[i][2] + '%<br>CPU使用率: ' + z_group_data[i][1] + '%<br>内存使用率: ' + z_group_data[i][0] + '%';
                var data = scope.nodeName[i] + '<br>CPU使用率: ' + z_group_data[i][1] + '%<br>内存使用率: ' + z_group_data[i][0] + '%';
                obj.push(data);
              }
              text.push(obj);
            }
            var data = [{
              z: groupData(response),
              text: text,
              hoverinfo: 'text',
              type: 'surface',
              "colorbar": {
                "tickcolor": "#e4e4e4",
                tickfont: {
                  "family": "微软雅黑",
                  size: 14,
                  color: '#e4e4e4'
                },
              },
              "scl": [
                [
                  "0.0",
                  "rgb(20,119,178)"
                ],
                [
                  "0.222222222222",
                  "rgb(20,119,178)"
                ],
                [
                  "0.555555555556",
                  "rgb(116,173,209)"
                ],
                [
                  "0.777777777778",
                  "rgb(171,217,233)"
                ],
                [
                  "1.0",
                  "rgb(254,224,144)"
                ]
              ],

            }];



            setTimeout(function() {
              plotly_width = (element.find('#idcUse')[0].clientWidth) * 0.95;
              plotly_height = (element.find('#idcUse')[0].clientHeight);

              var layout = {
                title: '',
                hovermode: "closest",
                plot_bgcolor: 'transparent',
                "font": {
                  "family": "\"Open sans\", verdana, arial, sans-serif",
                  "size": 16,
                },
                scene: {
                  domain: {
                    x: [0.00, 1],
                    y: [0, 1]
                  },
                  camera: {
                    center: {
                      x: -1,
                      y: 1.2,
                      z: -0.5
                    },
                    eye: {
                      x: 5.4,
                      y: 4.6,
                      z: 0.4
                    },
                    up: {
                      x: 0,
                      y: 0,
                      z: 1
                    }
                  },
                  aspectmode: "manuel",
                  aspectratio: {
                    x: 2.8,
                    y: 11.2,
                    z: 3.6
                  },
                  autosize: false,
                  xaxis: {
                    title: '',
                    backgroundcolor: "transparent",
                    gridcolor: "#e4e4e4",
                    showbackground: true,
                    zerolinecolor: "#e4e4e4",

                    tickfont: {
                      "family": "微软雅黑",
                      size: 13,
                      color: '#e4e4e4'
                    },
                    ticktext: ['内存使用率', 'CPU使用率'],
                    tickvals: [0, 0.9]
                  },
                  yaxis: {
                    title: '',
                    backgroundcolor: "transparent",
                    gridcolor: "#e4e4e4",
                    showbackground: true,
                    zerolinecolor: "#e4e4e4",
                    tickfont: {
                      "family": "微软雅黑",
                      size: 13,
                      color: '#e4e4e4'
                    },
                    ticktext: scope.nodeName,
                    tickvals: [0, 1, 2, 3, 4, 5, 6, 7, 8]
                  },
                  zaxis: {
                    title: '',
                    backgroundcolor: "transparent",
                    gridcolor: "#e4e4e4",
                    showbackground: true,
                    zerolinecolor: "#e4e4e4",
                    tickfont: {
                      size: 13,
                      color: '#e4e4e4'
                    },
                    ticksuffix: '%'
                  }
                },
                autosize: false,
                width: plotly_width,
                height: plotly_height,
                margin: {
                  l: 0,
                  r: 10,
                  b: 5,
                  t: 10,
                }
              };
              Plotly.newPlot('idcUse', data, layout, {
                displayModeBar: false
              });

            }, 2000);

            // setInterval(function() {
            //   var container = document.getElementById('idcUse');
            //   if (container) {
            //     dashboardService.getIdcUse().then(function(resTime) {
            //       if (response.data != resTime.data && resTime.data.body) {
            //         container.data[0].z = groupData(resTime);
            //         Plotly.redraw(container);
            //       }
            //     });
            //   }
            //
            // }, 4000)
          })

        }
      }
    }
  ]);

  dashboard.directive('wiservMapText', ['dashboardService',
    function(dashboardService) {
      return {
        restrict: 'ACE',
        template: '<canvas id="myCanvas" ></canvas>',
        link: function(scope, element, attrs) {
          function drawTextAlongArc(context, str, centerX, centerY, radius, angle) {
            var len = str.length,
              s;
            context.save();
            context.translate(centerX, centerY);
            context.rotate(-1 * angle / 2);
            context.rotate(-10 * (angle / len)); // 调整文字的起始偏移
            for (var n = 0; n < len; n++) {
              context.rotate(angle / len);
              context.save();
              context.translate(0, -1 * radius);
              s = str[n];
              context.fillText(s, 0, 0);
              context.restore();
            }
            context.restore();
          }

          // dashboardService.getDataInfoNum().then(function(res) {
          setTimeout(function() {
            var map_chart_clientheight = element.parent()[0].clientHeight;
            var map_chart_height = element.parent()[0].offsetHeight;
            $(element.find('canvas')).attr('width', map_chart_height);
            $(element.find('canvas')).attr('height', map_chart_height);

            var word_spacing = 4;
            if (map_chart_clientheight > 700) {
              word_spacing = 5;
            }

            var canvas = document.getElementById('myCanvas'),
              context = canvas.getContext('2d'),
              centerX = canvas.width / 2 + 24,
              centerY = canvas.height / 2 + 16,
              angle = Math.PI / word_spacing, //调整文字间距
              radius = map_chart_height / 2;

            context.font = '26px Calibri';
            context.textAlign = 'center';
            context.fillStyle = '#FFF';
            context.strokeStyle = 'transparent';
            context.lineWidth = 0;

            // var dataInfoNum = res.body[0].dataInfoNum;
            drawTextAlongArc(context, '社会化数据', centerX, centerY, radius, angle);

            // draw circle underneath text
            context.arc(centerX, centerY, radius - 10, 0, 2 * Math.PI, false);
            context.stroke();
            // })
          }, 600)



        }
      }
    }
  ])

  dashboard.directive('wiservMapTextDown', ['dashboardService',
    function(dashboardService) {
      return {
        restrict: 'ACE',
        template: '<canvas id="downCanvas" ></canvas>',
        link: function(scope, element, attrs) {
          function drawTextAlongArc(context, str, centerX, centerY, radius, angle, rotate_base) {
            var len = str.length,
              s;
            context.save();
            context.translate(centerX, centerY);
            context.rotate(-1 * angle / 1.5);
            context.rotate(rotate_base * (angle / len)); // 调整文字的起始偏移
            for (var n = 0; n < len; n++) {
              context.rotate(angle / len);
              context.save();
              context.translate(0, -1 * radius);
              s = str[n];
              context.fillText(s, 0, 0);
              context.restore();
            }
            context.restore();
          }

          //dashboardService.getDataInfoNum().then(function(res) {
          setTimeout(function() {
            var map_chart_clientheight = element.parent()[0].clientHeight;
            var map_chart_height = element.parent()[0].offsetHeight;
            $(element.find('canvas')).attr('width', map_chart_height);
            $(element.find('canvas')).attr('height', map_chart_height);

            var word_spacing = 4;
            var rotate_base = 12;
            if (map_chart_clientheight > 700) {
              word_spacing = 6;
              rotate_base = 19;
            }

            var canvas = document.getElementById('downCanvas'),
              context = canvas.getContext('2d'),
              centerX = canvas.width / 2 - 20,
              centerY = canvas.height / 2 - 30,
              angle = Math.PI / word_spacing, //调整文字间距
              radius = map_chart_height / 2;

            context.font = '26px Calibri';
            context.textAlign = 'center';
            context.fillStyle = '#FFF';
            context.strokeStyle = 'transparent';
            context.lineWidth = 0;

            drawTextAlongArc(context, '政务数据', centerX, centerY, radius, angle, rotate_base);

            // draw circle underneath text
            context.arc(centerX, centerY, radius - 10, 0, 2 * Math.PI, false);
            context.stroke();
          }, 600)

          //})
        }
      }
    }
  ])

  dashboard.directive('wiservMapChart', [
    function() {
      return {
        restrict: 'ACE',
        template: '<svg ></svg>',
        link: function(scope, element, attrs) {
          setTimeout(function() {
            var map_chart_clientheight = element.parent()[0].clientHeight;
            var map_chart_height = element.parent()[0].offsetHeight;
            $(element.parent()[0]).css('width', (map_chart_height) + 'px');
            $(element.parent()[0]).css('height', (map_chart_height) + 'px');
            $(element.find('svg')[0]).attr('width', map_chart_clientheight);
            $(element.find('svg')[0]).attr('height', map_chart_clientheight);

            var svg = d3.select(element.find('svg')[0]),
              width = +svg.attr("width"),
              height = +svg.attr("height"),
              g = svg.append("g").attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");

            var stratify = d3.stratify()
              .parentId(function(d) {
                return d.id.substring(0, d.id.lastIndexOf("."));
              });

            var tree_size = [285, 150];
            if (map_chart_clientheight > 700) {
              tree_size = [285, 230];
            }
            var tree = d3.tree()
              .size(tree_size)
              .separation(function(a, b) {
                return (a.parent == b.parent ? 1 : 1) / a.depth;
              });

            d3.csv("assets/file/flare.csv", function(error, data) {
              // var data = [{
              //   id:'data',
              //   value:''
              // },{
              //   id:'data.social',
              //   value:''
              // },{
              //   id:'data.gov',
              //   value:''
              // },{
              //   id:'data.social.交通及通讯',
              //   value:''
              // },{
              //   id:'data.social.交通及通讯1',
              //   value:''
              // },{
              //   id:'data.social.交通及通讯2',
              //   value:''
              // },{
              //   id:'data.social.交通及通讯3',
              //   value:''
              // },{
              //   id:'data.gov.交通及通讯',
              //   value:''
              // },{
              //   id:'data.gov.交通及通讯1',
              //   value:''
              // },{
              //   id:'data.gov.交通及通讯2',
              //   value:''
              // },{
              //   id:'data.gov.交通及通讯3',
              //   value:''
              // }];
              if (error) throw error;

              var root = tree(stratify(data));

              var link = g.selectAll(".link")
                .data(root.descendants().slice(1))
                .enter().append("path")
                .attr("class", "link")
                .attr("d", function(d) {
                  return "M" + project(d.x, d.y) + "C" + project(d.x, (d.y + d.parent.y) / 1.8) + " " + project(d.parent.x, (d.y + d.parent.y) / 3) + " " + project(d.parent.x, d.parent.y / 1.1);
                });

              var node = g.selectAll(".node")
                .data(root.descendants())
                .enter().append("g")
                .attr("class", function(d) {
                  return "node" + (d.children ? " node--internal" : " node--leaf");
                })
                .attr("transform", function(d) {
                  if (d.children) {
                    return "translate(" + project(d.x, d.y / 1.1) + ")";
                  }
                  return "translate(" + project(d.x, d.y) + ")";
                });

              node.append("circle")
                .attr("r", 9.5);

              node.append("text")
                .attr("dy", ".31em")
                .attr("x", function(d) {
                  return d.x < 180 === !d.children ? -12 : 12;
                })
                .style("text-anchor", function(d) {
                  return d.x < 180 === !d.children ? "end" : "start";
                })
                .attr("transform", function(d) {
                  return "rotate(" + (d.x < 180 ? d.x - 90 : d.x + 90) + ")";
                })
                .text(function(d) {
                  return d.id.substring(d.id.lastIndexOf(".") + 1);
                });
            });

          }, 600)

          function project(x, y) {
            var angle = (x - 90) / 180 * Math.PI,
              radius = y;
            return [-radius * Math.cos(angle), -radius * Math.sin(angle)];
          }
        }
      }
    }
  ])

  dashboard.directive('wiservNestData', [
    function() {
      return {
        restrict: 'ACE',
        template: '',
        // template: '<a class="nav a0" target="_blank" href="#"><s></s>块0<b></b></a>' +
        //   '<a class="nav a1" target="_blank" href="#"><s></s>块1<b></b></a>' +
        //   '<a class="nav a2" target="_blank" href="#"><s></s>块2<b></b></a>',
        link: function(scope, element, attrs) {
          // var parent_width = element.parent()[0].offsetWidth;
          // var parent_height = element.parent()[0].offsetHeight;
          // var a_width = element.find('a')[0].offsetWidth;
          // var a_height = element.find('a')[0].offsetHeight;
          // var s_width = element.find('s')[0].offsetWidth;
          // var s_height = element.find('s')[0].offsetHeight;
          // console.log(element.find('s'));
          // console.log(s_width);
          // console.log(s_height);
          // console.log(parent_height);
          // console.log(a_width);
          // console.log(a_height);
          //
          // var a0 = element.find('a.a0')[0];
          // a0.style.top = parent_height / 2 - a_height + 'px';
          // a0.style.left = '50px';
          //
          // var a1 = element.find('a.a1')[0];
          // a1.style.top = parent_height / 2 - 3 * a_height / 2 - 6 + 'px';
          // a1.style.left = a_width + s_width - 10 + 'px';
        }
      }
    }
  ])


  dashboard.directive('wiservStandardSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/standard/view.html",
      replace: true
    };
    //
  });

  dashboard.directive('wiservCenterSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/center/view.html",
      replace: true
    };
  });

  dashboard.directive('wiservDeptSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/dept/view.html",
      replace: true
    };
  });

  dashboard.directive('wiservSupportSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/support/view.html",
      replace: true
    };
  });

  dashboard.directive('wiservDatamapSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/datamap/view.html",
      replace: true
    };
  });

  dashboard.directive('wiservIdcuseSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/idcuse/view.html",
      replace: true
    };
  });
  dashboard.directive('wiservBigdataSection', function() {
    return {
      restrict: 'A',
      templateUrl: "partials/bigdata/view.html",
      replace: true
    };
  });

  dashboard.directive('wiservDashboard', ['$location', function(location) {
    return {
      restrict: 'A',
      templateUrl: "partials/dashboard/part.html",
      replace: true,
      link: function(scope, element, attrs) {
        var screen_width = screen.width;
        var screen_height = screen.height;
        var path = location.path();
        if (path.indexOf('main') > -1) {
          $('.section').css({
            'height': screen_height / 2 + 'px'
          });
        }
        // if(path.indexOf('dashboard')) {
        //   $('.section').css('width',)
        // }
      }
    };
  }]);

  dashboard.directive('wiservImagePlay', ['$location', function(location) {
    return {
      restrict: 'A',
      templateUrl: "partials/support/sub.html",
      replace: true,
      link: function(scope, element, attrs) {
        element.slick({
          slidesToShow: 4,
          slidesToScroll: 1,
          autoplay: true,
          autoplaySpeed: 3000,
        });
      }
    }
  }]);

  dashboard.directive('wiservImagePlaydown', ['$location', function(location) {
    return {
      restrict: 'A',
      templateUrl: "partials/support/subdown.html",
      replace: true,
      link: function(scope, element, attrs) {
        element.slick({
          slidesToShow: 4,
          slidesToScroll: 1,
          autoplay: true,
          autoplaySpeed: 3000,
        });
      }
    }
  }]);
})();