(function() {
  /** Module */
  var dashboard = angular.module('app.dashboard', [])
  dashboard.$inject = ['$location'];
  /** Controller */
  dashboard.controller('dashboardController', [
    '$scope', 'dashboardService', '$rootScope',
    function($scope, dashboardService, $rootScope) {
      var vm = this;
      $rootScope.change_flag = false; // 不切换
      // get deptartment data count
      dashboardService.getDeptDataQuantity().then(function(result) {
        var data = result.data.body[0];
        if (data) {
          vm.deptData = data;
        }
      })


      $scope.changeRoute = function() {
        dashboardService.getStatus().then(function(result) {
          var data = result.data;
          var newValue = "";
          if (data == 1) {
            newValue = "";
          } else {
            newValue = 1;
          }
          console.log(newValue);
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
        console.log(data);
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
        getDataInfoNum: getDataInfoNum
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
  //
  // dashboard.service('dashboard.chartIdcUse', ['$http', 'URL',
  //   function($http, URL) {
  //     if (URL) {
  //       return $http({
  //         method: 'GET',
  //         url: URL + '/serverMonitor/detail',
  //         withCredentials: true
  //       });
  //     } else {
  //       console.error('API Not Found in config.js');
  //     }
  //   }
  // ]);

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


  dashboard.directive('wiservDeptData', ['dashboard.chartDeptDataTable', 'dashboard.chartDeptDataColumn',
    function(deptDataTable, deptDataColumn) {
      return {
        restrict: 'ACE',
        template: "<div id='deptData' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          deptDataTable.then(function(response) {
              return response.data.body;
            }).then(function(tableData) {
              var table = tableData;
              deptDataColumn.then(function(rescolumn) {
                var column = rescolumn.data.body;
                _.forEach(table, function(tobj) {
                  _.forEach(column, function(cobj) {
                    if (tobj.depName == cobj.depName) {
                      tobj.columnNum = parseInt(cobj.columnNum);
                    }
                  });
                });
                var option = {
                  legend: {
                    data: [{
                      name: '数据集',
                      icon: 'rect'
                    }, {
                      name: '数据项',
                      icon: 'rect'
                    }],
                    itemWidth: 90,
                    itemHeight: 30,
                    padding: 10,
                    align: 'left',
                    textStyle: {
                      color: 'rgb(220,220,220)',
                      fontSize: 24
                    },
                    itemGap: 50,
                    top: 34,
                    left: '44%'
                  },
                  color: ['#3398DB'],
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: { // 坐标轴指示器，坐标轴触发有效
                      type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                    }
                  },
                  grid: {
                    top: '18%',
                    left: '3%',
                    right: '4%',
                    bottom: '6%',
                    containLabel: true
                  },
                  xAxis: [{
                    type: 'category',
                    data: _.map(table, 'depName'),
                    boundaryGap: true,
                    axisTick: {
                      alignWithLabel: false,
                      length: 12
                    },
                    axisLabel: {
                      interval: 0,
                      margin: 24,
                      textStyle: {
                        color: 'rgb(220,220,220)',
                        fontSize: 18
                      }
                    },
                    axisLine: {
                      lineStyle: {
                        color: 'rgba(204, 204, 204, 0.9)'
                      }
                    }
                  }],
                  yAxis: [{
                    type: 'value',
                    name: '数据集',
                    nameTextStyle: {
                      color: '#FFF',
                      fontSize: 18
                    },
                    nameGap: 24,
                    axisTick: {
                      show: true
                    },
                    axisLine: {
                      lineStyle: {
                        color: 'rgba(204, 204, 204, 0.9)'
                      }
                    },
                    axisLabel: {
                      textStyle: {
                        color: '#FFF',
                        fontSize: 18
                      }
                    },
                    splitLine: {
                      lineStyle: {
                        color: ['rgba(204, 204, 204, 0.5)']
                      }
                    }
                  }, {
                    type: 'value',
                    name: '数据项',
                    nameTextStyle: {
                      color: '#FFF',
                      fontSize: 18
                    },
                    nameGap: 24,
                    axisTick: {
                      show: true
                    },
                    axisLine: {
                      lineStyle: {
                        color: 'rgba(204, 204, 204, 0.9)'
                      }
                    },
                    axisLabel: {
                      textStyle: {
                        color: '#FFF',
                        fontSize: 18
                      }
                    },
                    splitLine: {
                      lineStyle: {
                        color: ['rgba(204, 204, 204, 0.5)']
                      }
                    }
                  }],
                  series: [{
                    name: '数据集',
                    type: 'bar',
                    barWidth: '32%',
                    itemStyle: {
                      normal: {
                        color: 'rgb(0,203,254)',
                        shadowColor: 'rgb(0, 230, 255)',
                        shadowBlur: 12
                      }
                    },
                    data: _.map(table, 'tableNum')
                  }, {
                    name: '数据项',
                    type: 'line',
                    smooth: false,
                    symbol: 'circle',
                    symbolSize: 12,
                    yAxisIndex: 1,
                    itemStyle: {
                      normal: {
                        color: 'rgb(234,255,0)',
                        shadowColor: 'rgb(234,255,200)',
                        shadowBlur: 10
                      }
                    },
                    data: _.map(table, 'columnNum')
                  }],
                  animationDelay: 500
                };

                var chartInstance = echarts.init((element.find('#deptData'))[0]);
                chartInstance.setOption(option);

                // setTimeout(function(){
                //   var box_width = element.find('#deptData')[0].clientWidth;
                //   $('#deptData').css({
                //     'width': box_width
                //   });
                //   chartInstance.clear();
                //   chartInstance.resize();
                //   chartInstance.setOption(option);
                // },1500)

                setInterval(function() {
                  var box_width = element.find('#deptData')[0].clientWidth;
                  // $('#deptData').css({
                  //   'width': box_width
                  // });
                  chartInstance.clear();
                  chartInstance.resize();
                  chartInstance.setOption(option);
                }, 3000);

              })
            })
            //var data = response.data.body[0];restore


          //})

        }
      }
    }
  ]);

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
            for(var i=0; i<z_group_data.length;i++) {
              var obj = [];
              for(var j=0; j<z_group_data[i].length; j++) {
                // var data =  scope.nodeName[i] + '<br>硬盘使用率: ' + z_group_data[i][2] + '%<br>CPU使用率: ' + z_group_data[i][1] + '%<br>内存使用率: ' + z_group_data[i][0] + '%';
                var data =  scope.nodeName[i] + '<br>CPU使用率: ' + z_group_data[i][1] + '%<br>内存使用率: ' + z_group_data[i][0] + '%';
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
        var len = str.length, s;
        context.save();
        context.translate(centerX, centerY);
        context.rotate(-1 * angle / 2);
        context.rotate(-10 * (angle / len));// 调整文字的起始偏移
        for(var n = 0; n < len; n++) {
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
        $(element.find('canvas')).attr('width',map_chart_height);
        $(element.find('canvas')).attr('height',map_chart_height);

        var word_spacing = 3;
        if(map_chart_clientheight>700) {
          word_spacing = 5;
        }

        var canvas = document.getElementById('myCanvas'),
          context = canvas.getContext('2d'),
          centerX = canvas.width / 2+20,
          centerY = canvas.height/2+20,
          angle = Math.PI/word_spacing ,//调整文字间距
          radius = map_chart_height/2;

        context.font = '26px Calibri';
        context.textAlign = 'center';
        context.fillStyle = '#FFF';
        context.strokeStyle = 'transparent';
        context.lineWidth =0;

        // var dataInfoNum = res.body[0].dataInfoNum;
        drawTextAlongArc(context, '社会化数据共216类', centerX, centerY, radius, angle);

        // draw circle underneath text
        context.arc(centerX, centerY, radius - 10, 0, 2 * Math.PI, false);
        context.stroke();
      // })
      },600)



      }
    }
  }])

  dashboard.directive('wiservMapTextDown', ['dashboardService',
    function(dashboardService) {
      return {
        restrict: 'ACE',
        template: '<canvas id="downCanvas" ></canvas>',
        link: function(scope, element, attrs) {
          function drawTextAlongArc(context, str, centerX, centerY, radius, angle,rotate_base) {
          var len = str.length, s;
          context.save();
          context.translate(centerX, centerY);
          context.rotate(-1 * angle / 2);
          context.rotate(rotate_base * (angle / len));// 调整文字的起始偏移
          for(var n = 0; n < len; n++) {
            context.rotate(angle / len);
            context.save();
            context.translate(0, -1 * radius);
            s = str[n];
            context.fillText(s, 0, 0);
            context.restore();
          }
          context.restore();
        }

        dashboardService.getDataInfoNum().then(function(res) {
          setTimeout(function() {
            var map_chart_clientheight = element.parent()[0].clientHeight;
            var map_chart_height = element.parent()[0].offsetHeight;
            $(element.find('canvas')).attr('width',map_chart_height);
            $(element.find('canvas')).attr('height',map_chart_height);

            var word_spacing = 4;
            var rotate_base = 14;
            if(map_chart_clientheight>700) {
              word_spacing = 6;
              rotate_base = 19;
            }

          var canvas = document.getElementById('downCanvas'),
            context = canvas.getContext('2d'),
            centerX = canvas.width / 2-20,
            centerY = canvas.height/2-20,
            angle = Math.PI/word_spacing ,//调整文字间距
            radius = map_chart_height/2;

          context.font = '26px Calibri';
          context.textAlign = 'center';
          context.fillStyle = '#FFF';
          context.strokeStyle = 'transparent';
          context.lineWidth =0;

          var dataInfoNum = '';
          if(res.data && res.data.body[0] && res.data.body[0].dataInfoNum) {
            dataInfoNum = res.data.body[0].dataInfoNum;
          }
          drawTextAlongArc(context, '政务数据共'+dataInfoNum+'类', centerX, centerY, radius, angle,rotate_base);

          // draw circle underneath text
          context.arc(centerX, centerY, radius - 10, 0, 2 * Math.PI, false);
          context.stroke();
        },600)

      })
        }
      }
    }])

  dashboard.directive('wiservMapChart', [
    function() {
      return {
        restrict: 'ACE',
        template: '<svg ></svg>',
        link: function(scope, element, attrs) {
          setTimeout(function() {
            var map_chart_clientheight = element.parent()[0].clientHeight;
            var map_chart_height = element.parent()[0].offsetHeight;
            $(element.parent()[0]).css('width',(map_chart_height)+'px');
            $(element.parent()[0]).css('height',(map_chart_height)+'px');
            $(element.find('svg')[0]).attr('width',map_chart_clientheight);
            $(element.find('svg')[0]).attr('height',map_chart_clientheight);

            var svg = d3.select(element.find('svg')[0]),
              width = +svg.attr("width"),
              height = +svg.attr("height"),
              g = svg.append("g").attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");

            var stratify = d3.stratify()
              .parentId(function(d) {
                return d.id.substring(0, d.id.lastIndexOf("."));
              });

            var tree_size = [285, 150];
            if(map_chart_clientheight>700) {
              tree_size = [285, 230];
            }
            var tree = d3.tree()
              .size(tree_size)
              .separation(function(a, b) {
                return (a.parent == b.parent ? 1 : 2) / a.depth;
              });

            d3.csv("assets/file/flare.csv", function(error, data) {
              console.log(data);
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
                  return "M" + project(d.x, d.y) + "C" + project(d.x, (d.y + d.parent.y) / 1.8) + " " + project(d.parent.x, (d.y + d.parent.y) / 3) + " " + project(d.parent.x, d.parent.y/1.1);
                });

              var node = g.selectAll(".node")
                .data(root.descendants())
                .enter().append("g")
                .attr("class", function(d) {
                  return "node" + (d.children ? " node--internal" : " node--leaf");
                })
                .attr("transform", function(d) {
                  if(d.children) {
                    return "translate(" + project(d.x, d.y/1.1) + ")";
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

          },600)

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
