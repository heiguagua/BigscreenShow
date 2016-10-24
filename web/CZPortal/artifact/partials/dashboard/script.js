(function() {
  /** Module */
  var dashboard = angular.module('app.dashboard', [])

  /** Controller */
  dashboard.controller('dashboardController', [
    '$scope', 'dashboardService',
    function($scope, dashboardService) {
      var vm = this;
      // get deptartment data count
      dashboardService.getDeptDataQuantity().then(function(result) {
        var data = result.data.body[0];
        if (data) {
          vm.deptData = data;
        }
      })
    }
  ]);

  /** Service */
  dashboard.factory('dashboardService', ['$http', 'URL',
    function($http, URL) {
      return {
        getDeptDataQuantity: getDeptDataQuantity
      }

      function getDeptDataQuantity() {
        return $http.get(
          URL + '/depDataInfo/quantity'
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

  dashboard.service('dashboard.chartIdcUse', ['$http', 'URL',
    function($http, URL) {
      if (URL) {
        return $http({
          method: 'GET',
          url: URL + '/serverMonitor/detail',
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
          var option = {
            title: {
              text: '对接进度',
              left: 'center',
              top: 15,
              textStyle: {
                color: 'rgb(0,225,252)',
                fontSize: 36,
                fontWeight: 'normal'
              }
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
              radius: 218,
              splitNumber: 7,
              name: {
                formatter: '{value}',
                textStyle: {
                  color: '#FFF',
                  fontSize: 16
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
                name: '图一',
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
                itemStyle:{
                        normal:{
                            color:'rgb(69,128,180)'
                        }
                    }
              }]
            }]
          }
          echarts.init((element.find('#dockProgress'))[0]).setOption(option);
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
          var option = {
            title: {
              text: '对接后采集到平台的数据项',
              left: 'center',
              top: 48,
              textStyle: {
                color: 'rgb(0,225,252)',
                fontSize: 36,
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
                  color: '#FFF',
                  fontSize: 16
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
                  color: '#FFF'
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
                    color: '#FFF'
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
              data: [6, 0, 5, 0, 6, 18, 8, 27, 14]
            }]
          };


          echarts.init((element.find('#dockDataset'))[0]).setOption(option);
        }
      }
    }
  ]);


  dashboard.directive('wiservDeptData', ['dashboard.chartDeptDataTable', 'dashboard.chartDeptDataColumn',
    function(deptDataTable, deptDataColumn) {
      return {
        restrict: 'ACE',
        template: "<div id='deptData'></div>",
        link: function(scope, element, attrs) {
          deptDataTable.then(function(response) {
              return response.data.body;
            }).then(function(table) {
              deptDataColumn.then(function(rescolumn) {
                var table = table;
                var column = rescolumn.data.body;
              })
            })
            //var data = response.data.body[0];
          var option = {
            legend: {
              data: [{
                name: '大类',
                icon: 'rect'
              }, {
                name: '小类',
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
              data: ['城管局', '规划局', '国土局', '地税局', '公安局', '交通局', '社保局', '安监局', '安检局'],
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
              name: '大类',
              type: 'bar',
              barWidth: '32%',
              itemStyle: {
                normal: {
                  color: 'rgb(0,203,254)',
                  shadowColor: 'rgb(0, 230, 255)',
                  shadowBlur: 12
                }
              },
              data: [58, 45, 27, 29, 52, 64, 43, 73, 37]
            }, {
              name: '小类',
              type: 'line',
              smooth: false,
              symbol: 'circle',
              symbolSize: 12,
              itemStyle: {
                normal: {
                  color: 'rgb(234,255,0)',
                  shadowColor: 'rgb(234,255,200)',
                  shadowBlur: 10
                }
              },
              data: [12, 29, 13, 33, 32, 48, 40, 69, 21]
            }]
          };
          echarts.init((element.find('#deptData'))[0]).setOption(option);
          //})

        }
      }
    }
  ]);

  dashboard.directive('wiservIdcUse', ['dashboard.chartIdcUse',
    function(idcUseService) {
      return {
        restrict: 'ACE',
        template: "<div id='idcUse' style='width:100%;height:100%'></div>",
        link: function(scope, element, attrs) {
          idcUseService.then(function(response) {
            var rateData = response.data.body;
            scope.memRateList = _.map(rateData,'memRate');
            scope.cpuRateList = _.map(rateData,'cpuRate');
            scope.diskRateList = _.map(rateData,'diskRate');
            var rows = [];
            for (var i = 0; i < 3; i++) {
              var monitors = {};
              monitors[""] = i.toString();
              switch (i) {
                case 0:
                  for (var j = 0; j < rateData.length; j++) {
                    console.log(rateData[j]);
                    monitors[j] = rateData[j].memRate.toString();
                  }
                  break;
                case 1:
                  for (var j = 0; j < rateData.length; j++) {
                    monitors[j] = rateData[j].cpuRate.toString();
                  }
                  break;
                case 2:
                  for (var j = 0; j < rateData.length; j++) {
                    monitors[j] = rateData[j].diskRate.toString();
                  }
                  break;
                default:
                  break;
              }
              rows.push(monitors);
            }
            scope.nodeName = _.map(rateData, 'nodeName');
            console.log(rows);

            // var rows = [{
            //   "": "0",
            //   0: "70.15",
            //   1: '76.77',
            //   2: "61.39",
            //   3: '59.32',
            //   4: '62.58',
            //   5: '28.89',
            //   6: '58.43',
            //   7: '32.54',
            //   8: '35.42'
            // }, {
            //   "": "1",
            //   0: "0",
            //   1: "0",
            //   2: '48.32',
            //   3: '12.56',
            //   4: '19.68',
            //   5: '9.15',
            //   6: '2.7',
            //   7: '35.13',
            //   8: '12.43'
            // }, {
            //   "": "2",
            //   0: "0",
            //   1: "0",
            //   2: '3.48',
            //   3: '2.61',
            //   4: '2.47',
            //   5: '1.24',
            //   6: '3.41',
            //   7: '5.76',
            //   8: '6.87'
            // }];

            function unpack(rows, key) {
              return rows.map(function(row) {
                return row[key];
              });
            }

            var z_data = []
            for (var i = 0; i < 9; i++) {
              z_data.push(unpack(rows, i));
            }
            var data = [{
              z: z_data,
              type: 'surface'
            }];

            var layout = {
              title: 'Mt Bruno Elevation',
              plot_bgcolor: 'transparent',
              scene: {
                domain: {
                  x: [0.00, 1],
                  y: [-0.5, 1]
                },
                camera: {
                  center: {
                    x: -1.1,
                    y: 0,
                    z: -1
                  },
                  eye: {
                    x: 5.4,
                    y: 4.6,
                    z: 0.4
                  },
                  up: {
                    x:0,
                    y: 0,
                    z: 1
                  }
                },
                aspectmode: "manuel",
                aspectratio: {
                  x:1.8,
                  y:9,
                  z: 3.6
                },
                autosize: false,
                xaxis: {
                  title: '',
                  backgroundcolor: "transparent",
                  gridcolor: "rgb(230, 230, 230)",
                  showbackground: true,
                  zerolinecolor: "rgb(230, 230, 230)",
                  tickfont: {
                    size: 14,
                    color: 'rgb(230, 230, 230)'
                  },
                  ticktext: ['内存使用率', 'CPU使用率', '硬盘使用率'],
                  tickvals: [0, 1, 2]
                },
                yaxis: {
                  title: '',
                  backgroundcolor: "transparent",
                  gridcolor: "rgb(230, 230, 230)",
                  showbackground: true,
                  zerolinecolor: "rgb(230, 230, 230)",
                  tickfont: {
                    size: 14,
                    color: 'rgb(230, 230, 230)'
                  },
                  ticktext: scope.nodeName,
                  tickvals: [0, 1, 2, 3, 4, 5, 6, 7, 8]
                },
                zaxis: {
                  title: '',
                  backgroundcolor: "transparent",
                  gridcolor: "rgb(230, 230, 230)",
                  showbackground: true,
                  zerolinecolor: "rgb(230, 230, 230)",
                  tickfont: {
                    size: 14,
                    color: 'rgb(230, 230, 230)'
                  },
                  ticksuffix: '%'
                }
              },
              autosize: false,
              width: '1550',
              height: '670',
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
          })

        }
      }
    }
  ]);

  dashboard.directive('wiservMapChart', [
    function() {
      return {
        restrict: 'ACE',
        template: '<svg width="730" height="730" ></svg>',
        link: function(scope, element, attrs) {
          var svg = d3.select(element.find('svg')[0]),
            width = +svg.attr("width"),
            height = +svg.attr("height"),
            g = svg.append("g").attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");

          var stratify = d3.stratify()
            .parentId(function(d) {
              return d.id.substring(0, d.id.lastIndexOf("."));
            });

          var tree = d3.tree()
            .size([230, 230])
            .separation(function(a, b) {
              return (a.parent == b.parent ? 1 : 2) / a.depth;
            });

          d3.csv("assets/file/flare.csv", function(error, data) {
            if (error) throw error;

            var root = tree(stratify(data));

            var link = g.selectAll(".link")
              .data(root.descendants().slice(1))
              .enter().append("path")
              .attr("class", "link")
              .attr("d", function(d) {
                return "M" + project(d.x, d.y) + "C" + project(d.x, (d.y + d.parent.y) / 2) + " " + project(d.parent.x, (d.y + d.parent.y) / 2) + " " + project(d.parent.x, d.parent.y);
              });

            var node = g.selectAll(".node")
              .data(root.descendants())
              .enter().append("g")
              .attr("class", function(d) {
                return "node" + (d.children ? " node--internal" : " node--leaf");
              })
              .attr("transform", function(d) {
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

          function project(x, y) {
            var angle = (x - 90) / 180 * Math.PI,
              radius = y;
            return [-radius * Math.cos(angle), -radius * Math.sin(angle)];
          }
        }
      }
    }
  ])



})();
