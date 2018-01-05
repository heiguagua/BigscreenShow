;
(function() {
	/** Module */
	var dept = angular.module('app.dept', [])
	dept.$inject = ['$location'];
	/** Controller */
	dept.controller('deptController', [
		'$scope', 'deptService', '$rootScope', '$interval', '$timeout',
		function($scope, deptService, $rootScope, $interval, $timeout) {

			$scope.toggleMap = 2;
			$scope.update_date = new Date();
			$scope.dept_rank_slicked = false;
			//部门视图鼠标进入
			$scope.allow_toggle = true;
			$scope.mouseEnter = function() {
				$scope.allow_toggle = false;
			}
			//部门视图鼠标移除
			$scope.mouseLeave = function() {
				$scope.allow_toggle = true;

			}

			// 信息资源目录和共享情况内容切换
			$scope.toggleDept = function() {
				if ($scope.allow_toggle) { // 鼠标在视图内，禁止自动切换

					var temp = angular.copy($scope.toggleMap);
					$scope.toggleMap = (temp + 1) % 3;
					if ($scope.toggleMap == 1) {
						if(!$scope.dept_rank_slicked){
							$timeout(function() {
								$(".dept-access-rank").slick({
									slidesToShow: 8,
									slidesToScroll: 1,
									autoplay: true,
									autoplaySpeed: 2500,
									vertical: true,
									verticalSwiping: true
								});
							}, 100);
							$scope.dept_rank_slicked = true;
						}
						else{
							$('.dept-access-rank').slick('slickPlay');
							
						}
						
					}
					else{
						if($scope.dept_rank_slicked){
							$('.dept-access-rank').slick('slickPause');
						}
						
					}
					if($scope.toggleMap != 2){
						$('.profile-right-wrap').slick('slickPause');
					}
					else{
						$('.profile-right-wrap').slick('slickPlay');
					}


				}

			}

			$scope.deptToggleClick = function() { // 部门视图切换
				$scope.allow_toggle = true;
				$scope.toggleDept();
			}

			$interval(function() {
				$scope.toggleDept();
			}, 50000)

			$interval(function() { // 按1小时更新的数据
				getDataCount(); // 信息资源目录和共享情况
				$scope.update_date = new Date();
			}, 3600000);

			// 获取信息资源目录和共享情况统计数
			var getDataCount = function() {
				// 获取已汇集数据量及最新一周新增
				deptService.getDataCountLast().then(function(result) {
					$scope.dataCountLast = result.data.body;
				})

				// 获取其他数据
				deptService.getResourceCombing().then(function(result) {
					$scope.ResourceCombing = result.data.body[0];
					$timeout(function() {
						
						$(".profile-right-wrap").slick({
							slidesToShow: 3,
							slidesToScroll: 1,
							autoplay: true,
							autoplaySpeed: 2500
						});
						
					}, 10);
				})

				// 部门已接入数据总量
				deptService.getAccessDataNum().then(function(result) {
					$scope.deptAccessData = result.data.body;
					$scope.deptAccessMax = _.max(_.map($scope.deptAccessData, 'dataNum')) + 30;
					_.forEach($scope.deptAccessData, function(item) {
						item.i_width = item.dataNum / $scope.deptAccessMax * 100 + '%';
					})
					


				})


			}

			function init() {
				getDataCount();
			}

			init();

		}

	])

	/** Service */
	dept.factory('deptService', ['$http', 'URL',
		function($http, URL) {
			return {
				getDataCountLast: getDataCountLast,
				getResourceCombing: getResourceCombing,
				getAccessDataNum: getAccessDataNum,
				getDeptCoords: getDeptCoords,
				getDeptContract: getDeptContract, //崇信签约或者意向签约的数据资源
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

			function getDeptContract() {
				return $http.get(
					'assets/file/dept_contract.json'
				)
			}

		}
	])

	dept.directive('wiservChartMigrate', ['deptService',
		function(deptService) {
			return {
				restrict: 'ACE',
				template: "<div id='deptData' style='width:100%;height:100%'></div>",
				link: function(scope, element, attrs) {
					var chartInstance = echarts.init((element.find('#deptData'))[0]);
					deptService.getDeptCoords().then(function(result) {
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

	dept.directive('wiservChartContract', ['deptService',
		function(deptService) {
			return {
				restrict: 'ACE',
				template: "<div id='deptData' style='width:100%;height:100%'></div>",
				link: function(scope, element, attrs) {
					var chartInstance = echarts.init((element.find('#deptData'))[0]);
					deptService.getDeptContract().then(function(result) {
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
										if (data[i].name == "成都医保数据" || data[i].name == "舆情数据" || data[i].name == "行政审批数据") {
											if (data[i].name == "行政审批数据") {
												tGeoDt.push({
													coords: [geoData[srcNam], geoData[data[i].name]],
													lineStyle: {
														normal: {
															curveness: 0.18,
														}
													}
												});

											} else if (data[i].name == "成都医保数据") {
												tGeoDt.push({
													coords: [geoData[srcNam], geoData[data[i].name]],
													lineStyle: {
														normal: {
															curveness: 0.3,
														}
													}
												});
											} else {
												tGeoDt.push({
													coords: [geoData[srcNam], geoData[data[i].name]],
												});
											}

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
									if (tNam == '国家资源卫星数据') {
										tGeoDt.push({
											name: tNam,
											value: geoData[tNam],
											symbol: 'circle',
											symbolSize: 14,
											label: {
												normal: {
													position: [-80, -100]
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
													align: 'right',
													position: [-30, 30],
													rich: {
														b: {
															align: 'right',
														},
														c: {
															align: 'right',
														}
													}
												}
											}
										});
									} else if (tNam == '社会信用数据') {
										tGeoDt.push({
											name: tNam,
											value: geoData[tNam],
											symbolSize: 14,
											label: {
												normal: {
													align: 'right',
													position: [-0, 25]
												}
											}
										});
									} else if (tNam == '行政审批数据') {
										tGeoDt.push({
											name: tNam,
											value: geoData[tNam],
											symbolSize: 14,
											label: {
												normal: {
													position: [-20, 35]
												}
											}
										});
									} else if (tNam == '知识产权数据') {
										tGeoDt.push({
											name: tNam,
											value: geoData[tNam],
											symbolSize: 14,
											label: {
												normal: {
													position: [40, -70]
												}
											}
										});
									} else if (tNam == '成都医保数据') {
										tGeoDt.push({
											name: tNam,
											value: geoData[tNam],
											symbolSize: 14,
											label: {
												normal: {
													position: [25, -10]
												}
											}
										});
									} else {
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
								symbol: 'image://' + location.protocol + '//' + location.host + '/CZPortal/assets/images/sign.png',
								symbolSize: 50,
								label: {
									normal: {
										position: [20, -80],
										rich: {
											a: {
												color: '#edfc02',
												fontSize: 26
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
							fontSize = 18;
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
										formatter: function(params) {
											var data_arra = params.name.split('\n');
											if (data_arra.length > 1) {
												if (data_arra.length > 2) {
													return '{a|' + data_arra[0] + '}\n{b|' + data_arra[1] + '}{c|' + data_arra[2] + '}';
												}
												return '{a|' + data_arra[0] + '}\n{b|' + data_arra[1] + '}';
											}
											return '{a|' + data_arra[0] + '}';
										},
										rich: {
											a: {
												color: '#3bbbf0',
												fontSize: 26,
												lineHeight: 40,
											},
											b: {
												color: '#3bbbf0',
												fontSize: 26,
												fontFamily: 'digiFont',
												verticalAlign: 'bottom',
												padding: [0, 10, 0, 0]
											},
											c: {
												color: '#3bbbf0',
												fontSize: 20,
												verticalAlign: 'bottom',
											}
										},
										textStyle: {
											color: '#3bbbf0',
											fontSize: 20,
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

})()