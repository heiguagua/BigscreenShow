;
(function() {
	/** Module */
	var catalog = angular.module('app.catalog', []);
	/** Controller */
	catalog.controller('catalogController', [
		'$scope', 'catalogService', '$rootScope', '$interval', '$timeout',
		function($scope, catalogService, $rootScope, $interval, $timeout) {
			var rotationSpeed = 3000;
			var wrap = {
				x: 530,
				y: 670,
				radius: 320,
				radius_inner: 310
			};
			var dept_text_color = {
				normal: "rgb(31,234,193)",
				num_bg: "rgba(50,193,253,.23)",
				num: "#09a1df",
				num_active_bg: "rgb(51,178,127)",
				num_active: "#FFF"
			};
			var town_text_color = {
				normal: "rgb(9,161,223)",
				num_bg: "rgba(50,193,253,.23)",
				num: "rgb(50,193,253)",
				num_active_bg: "rgb(62,136,236)",
				num_active: "#FFF"
			};
			$scope.data1 = ["市安监局", "市财政局", "市城管局", "市地税局", "市发改局", "市法制办", "市公安局", "市规划局", "市国税局", "市国土局",
				"市环保局", "市交通局", "市教育局", "市经信局", "市民政局", "市农发局", "市气象局", "市人社局", "市商投局", "市审计局",
				"市市场监管", "市水务局", "市司法局", "市统计局", "市卫计局", "市文旅局", "市政务中心", "市住建局", "市综合执法",
				"崇信大数据", "市供电分公司"
			];
			$scope.data2 = ["崇阳街道", "羊马镇", "崇平镇", "梓潼镇", "廖家镇", "三江镇", "江源镇", "大划镇", "王场镇", "白头镇",
				"道明镇", "济协乡", "隆兴镇", "桤泉镇", "集贤乡", "燎原乡", "元通镇", "观胜镇", "锦江乡", "公议乡",
				"怀远镇", "街子镇", "三郎镇", "文井江镇", "鸡冠山乡"
			];
			$scope.data_number1 = ["256", "301", "145", "344", "53", "256", "301", "145", "344", "53",
				"301", "145", "344", "53", "256", "53", "256", "301", "145", "344",
				"53", "256", "53", "256", "301", "145", "344", "145", "344", "53",
				"45"
			];
			$scope.data_number2 = ["301", "145", "344", "53", "256", "53", "256", "301", "145", "344",
				"53", "256", "53", "256", "301", "145", "344", "145", "344", "53",
				"53", "256", "301", "145", "344"
			];

			$scope.data_code1 = [];
			$scope.data_code2 = [];

			$scope.DeptCataData = {}; // 单个市级部门目录数据信息
			$scope.TownCataData = {}; // 单个乡镇目录数据时信息

			var active_index = 16;
			var active_index_down = 13;

			var canvas = null;

			catalogService.getDeptCatalog().then(function(res) {
				if (res && res.data) {
					circleDataFormat(res);

					setTimeout(function() {
						canvas = this.__canvas = new fabric.Canvas('mycanvas');
						var each_degree = 360 / $scope.data_total_length;

						function splice_arr(type) {
							if (type == 1) {
								$scope.data1.splice(0, 0, $scope.data1[$scope.lens - 1]);
								$scope.data1.splice($scope.lens, 1);
								$scope.data_number1.splice(0, 0, $scope.data_number1[$scope.lens - 1]);
								$scope.data_number1.splice($scope.lens, 1);
								$scope.data_code1.splice(0, 0, $scope.data_code1[$scope.lens - 1]);
								$scope.data_code1.splice($scope.lens, 1);
							}
							if (type == 2) {
								$scope.data2.splice(0, 0, $scope.data2[$scope.len2 - 1]);
								$scope.data2.splice($scope.len2, 1);
								$scope.data_number2.splice(0, 0, $scope.data_number2[$scope.len2 - 1]);
								$scope.data_number2.splice($scope.len2, 1);
								$scope.data_code2.splice(0, 0, $scope.data_code2[$scope.len2 - 1]);
								$scope.data_code2.splice($scope.len2, 1);
							}
						}


						$scope.draw = function() {
							canvas.clear();
							for (var i = $scope.data_total_length - 1; i >= 0; i--) {

								// 得出每个圆点需要变换的弧度
								var corner = (i + 10.5) * each_degree;
								var radian = ((i + 10.5) * each_degree) * Math.PI / 180;

								var corner_town = (i - 14.5) * each_degree;
								var radian_town = ((i - 14.5) * each_degree) * Math.PI / 180;

								var text_angle = 0;
								var text_angle_town = 2;

								var text_origin_x = 'left';
								var text_origin_x_town = 'left';

								var rect = new fabric.Rect({
									width: 50,
									height: 26,
									centeredRotation: true,
									fill: dept_text_color.num_bg,
									originX: 'center',
									originY: 'center',
									rx: 6,
									ry: 6,
									left: wrap.x + Math.round(wrap.radius_inner * Math.cos(radian)),
									top: wrap.y + Math.round(wrap.radius_inner * Math.sin(radian)),
									angle: corner
								});

								if (Math.cos(radian) > 0) { // 右
								} else {
									text_angle = 180;
									text_origin_x = 'right';
								}
								var group1 = groupMaker(1, i, $scope.data_number1[i], $scope.data1[i], $scope.data_code1[i], text_origin_x, text_angle, corner, radian);
								canvas.add(group1);
								animatePlanet(group1, i, each_degree, corner, $scope.lens);

								var group2 = groupMaker(2, i, $scope.data_number2[i], $scope.data2[i], $scope.data_code2[i], text_origin_x_town, text_angle_town, corner_town, radian_town);
								canvas.add(group2);
								animatePlanet(group2, i, each_degree, corner_town, $scope.len2);

							}


						}

						init();
						$scope.timer1 = $interval(function() {

							$scope.draw();
							getDataByDept(1, $scope.data_code1[active_index], $scope.data1[active_index]);
							getDataByDept(2, $scope.data_code2[active_index_down], $scope.data2[active_index_down]);
						}, 5000)

						canvas.on('mouse:over', function(e) {
							var n = e.target;
							if ((n && n.dept_data) || (n && n.dept_number)) {
								$interval.cancel($scope.timer1);
								$scope.timer1 = null;
								if (n.g_type == 1 && n.dept_data.text != '') {
									if ("崇信大数据" != n.dept_data.text && "市供电分公司" != n.dept_data.text) {
										n.dept_data.set({
											'fill': dept_text_color.num_active
										});
									}

									n.bg_rect.set({
										'fill': dept_text_color.num_active_bg
									});
									n.dept_number.set({
										'fill': dept_text_color.num_active
									});
								}
								if (n.g_type == 2 && n.dept_data.text != '') {
									n.dept_data.set({
										'fill': town_text_color.num_active
									});
									n.bg_rect.set({
										'fill': town_text_color.num_active_bg
									});
									n.dept_number.set({
										'fill': town_text_color.num_active
									});
								}
							}
							canvas.renderAll();
						});

						canvas.on('mouse:out', function(e) {
							var n = e.target;
							if ((n && n.dept_data) || (n && n.dept_number)) {

								if (n.g_type == 1 && n.dept_data.text != '' && active_index != n.g_index) {
									if ("崇信大数据" != n.dept_data.text && "市供电分公司" != n.dept_data.text) {
										n.dept_data.set({
											'fill': dept_text_color.normal
										});
									}

									n.dept_number.set({
										'fill': dept_text_color.num
									});
									n.bg_rect.set({
										'fill': dept_text_color.num_bg
									});
								}
								if (n.g_type == 2 && n.dept_data.text != '' && active_index_down != n.g_index) {
									n.dept_data.set({
										'fill': town_text_color.normal
									});
									n.dept_number.set({
										'fill': town_text_color.num
									});
									n.bg_rect.set({
										'fill': town_text_color.num_bg
									});
								}
								$interval.cancel($scope.timer1);
								$scope.timer1 = $interval(function() {
									$scope.draw();
									getDataByDept(1, $scope.data_code1[active_index], $scope.data1[active_index]);
									getDataByDept(2, $scope.data_code2[active_index_down], $scope.data2[active_index_down]);
								}, 5000)
							}
							canvas.renderAll();
						});

						canvas.on('mouse:down', function(e) {
							var n = e.target;
							if ((n && n.dept_data) || (n && n.dept_number)) {
								if (n.dept_data.text != '') {
									getDataByDept(n.g_type, n.dept_data.dept_code, n.dept_data.text);
								}

							}
						})

						function animatePlanet(oImg, planetIndex, each_degree, corner, lens) {

							var radius = wrap.radius_inner,

								// rotate around canvas center
								cx = wrap.x,
								cy = wrap.y,
								rotationSpeed = 3000,
								// speed of rotation slows down for further planets
								duration = rotationSpeed,

								// randomize starting angle to avoid planets starting on one line
								startAngle = corner,
								endAngle = startAngle + each_degree;
							var index = 0;
							(function animate() {
								fabric.util.animate({
									startValue: startAngle,
									endValue: endAngle,
									duration: duration,

									easing: fabric.util.ease.easeOutBounce,

									onChange: function(angle) {

										var angle_temp = angle;
										angle = fabric.util.degreesToRadians(angle);
										var x = cx + (radius) * Math.cos(angle);
										var y = cy + (radius) * Math.sin(angle);

										oImg.set({
											left: x,
											top: y,
											angle: corner + each_degree
										}).setCoords();
										if (active_index == planetIndex && oImg.g_type == 1) {
											oImg.dept_data.set({
												'fill': dept_text_color.num_active
											});
											oImg.bg_rect.set({
												'fill': dept_text_color.num_active_bg
											});
											oImg.dept_number.set({
												'fill': dept_text_color.num_active
											});
										}
										if (active_index_down == planetIndex && oImg.g_type == 2) {
											oImg.dept_data.set({
												'fill': town_text_color.num_active
											});
											oImg.bg_rect.set({
												'fill': town_text_color.num_active_bg
											});
											oImg.dept_number.set({
												'fill': town_text_color.num_active
											});
										}
										if ("崇信大数据" == oImg.dept_data.text || "市供电分公司" == oImg.dept_data.text) {
											oImg.dept_data.set({
												'fill': "#c8eb24"
											});
										}
										// only render once
										if (planetIndex === $scope.data_total_length - 1) {

											if (angle_temp == endAngle) {
												if (oImg.g_type == 1) {
													splice_arr(1);
												}
												if (oImg.g_type == 2) {
													splice_arr(2);
												}
											}

											canvas.renderAll();
										}
									},
									onComplete: function() {

									}
								});
							})();
						}

						function groupMaker(type, index, text_number, text_dept, dept_code, origin_x, text_angle, corner, radian) {
							var bg_rect = dept_text_color.num_bg;
							var text_color = dept_text_color.normal;
							var number_color = dept_text_color.num;
							if (type == 2) {
								text_color = town_text_color.normal;
							}
							if (text_number == "") {
								bg_rect = 'transparent';
							}
							if (text_dept == "崇信大数据" || text_dept == "市供电分公司") {
								text_color = "#c8eb24";
							}
							var rect = new fabric.Rect({
								width: 42,
								height: 26,
								fill: bg_rect,
								originX: 'left',
								originY: 'left',
								rx: 6,
								ry: 6,
								centeredRotation: true,
							});

							var text = new fabric.Text("" + text_number, {
								fontSize: 20,
								fill: number_color,
								originX: origin_x,
								originY: 'center',
								centeredRotation: true,
								left: 3,
								angle: text_angle,
							});

							var group = new fabric.Group([rect, text], {
								centeredRotation: true,
								selectable: false,
								hasControls: false,
								hoverCursor: 'pointer',
								left: 145
							});



							var text2 = new fabric.Text(text_dept, {
								centeredRotation: true,
								fontSize: 23,
								dept_code: dept_code,
								fill: text_color,
								originX: origin_x,
								originY: 'center',
								angle: text_angle,
							});
							var group2 = new fabric.Group([text2, group], {
								centeredRotation: true,
								subtargets: false,
								selectable: false,
								hasControls: false,
								hoverCursor: 'pointer',
								left: wrap.x + Math.round(wrap.radius_inner * Math.cos(radian)),
								top: wrap.y + Math.round(wrap.radius_inner * Math.sin(radian)),
								angle: corner
							});
							group2.dept_data = text2;
							group2.dept_number = text;
							group2.bg_rect = rect;
							group2.g_type = type;
							group2.g_index = index;

							return group2;
						}



						function getDataByDept(dataType, deptCode, deptName) {

							if (dataType == 1) {
								$scope.DeptCataData.deptName = deptName;
							} else {
								$scope.TownCataData.deptName = deptName;
							}
							catalogService.getDataByDept({
								"dept_code": deptCode
							}).then(function(res) {
								if (res && res.data && res.data.body) {
									var data = res.data.body[0].num;
									if (dataType == 1) {
										$scope.DeptCataData.catalog = data;
									} else {
										$scope.TownCataData.catalog = data;
									}
								}


							})
							catalogService.getDataItemByDept({
								"dept_code": deptCode
							}).then(function(res) {
								if (res && res.data && res.data.body) {
									var data = res.data.body[0].num;
									if (dataType == 1) {
										$scope.DeptCataData.dataItem = data;
									} else {
										$scope.TownCataData.dataItem = data;
									}
								}


							})
							catalogService.getSystemByDept({
								"dept_code": deptCode
							}).then(function(res) {
								if (res && res.data && res.data.body) {
									var data = res.data.body[0].num;
									if (dataType == 1) {
										$scope.DeptCataData.system = data;
									} else {
										$scope.TownCataData.system = data;
									}
								}


							})
							catalogService.getResByDept({
								"dept_code": deptCode
							}).then(function(res) {
								if (res && res.data && res.data.body) {
									var data = res.data.body[0].num;
									if (dataType == 1) {
										$scope.DeptCataData.resource = data;
									} else {
										$scope.TownCataData.resource = data;
									}
								}


							})
						}

						function getDataCount() {
							// 获取已汇集数据量及最新一周新增
							catalogService.getDataCountLast().then(function(result) {
								$scope.dataCountLast = result.data.body;
							})
							// 获取应用调用次数
							catalogService.getInvokeNum().then(function(result) {
								$scope.invokeNum = result.data.body[0].num;
							})
						}

						function init() {
							$scope.draw();
							getDataByDept(1, $scope.data_code1[active_index], $scope.data1[active_index]);
							getDataByDept(2, $scope.data_code2[active_index_down], $scope.data2[active_index_down]);
							getDataCount();
						}
					}, 500)
				}
			})

			function circleDataFormat(res) {
				$scope.DeptCataData = {};
				$scope.TownCataData = {};
				var result = res.data.body;
				var grouped_data = _.groupBy(result, function(n) {
					return n.dept_type;
				});
				var data1_temp = grouped_data["C"];
				var data_ent = grouped_data["Z"];
				var union_data = _.union(data1_temp, data_ent);
				$scope.data1 = _.map(union_data, "dept_short_name");
				$scope.data2 = _.map(grouped_data["X"], "dept_short_name");

				$scope.data_number1 = _.map(union_data, "num");
				$scope.data_number2 = _.map(grouped_data["X"], "num");

				$scope.data_code1 = _.map(union_data, "dept_code");
				$scope.data_code2 = _.map(grouped_data["X"], "dept_code");

				$scope.lens = $scope.data1.length;
				$scope.len2 = $scope.data2.length;

				$scope.data_total_length = $scope.data1.length + $scope.data2.length;
				for (var i = 0; i < $scope.data_total_length; i++) {
					if (!$scope.data1[i]) {
						$scope.data1.push('');
						$scope.data_number1.push('');
						$scope.data_code1.push('');
					}
					if (!$scope.data2[i]) {
						$scope.data2.push('');
						$scope.data_number2.push('');
						$scope.data_code2.push('');
					}

				}
			}

			// 定时器，每天更新数据
			$interval(function() {
				// 市级部门及乡镇统计数据
				catalogService.getDeptCatalog().then(function(res) {
					circleDataFormat(res);
				})
				// 
			}, 86400000)

		}
	]);

	/** Service */
	catalog.factory('catalogService', ['$http', 'URL',
		function($http, URL) {
			return {
				"getDeptCatalog": getDeptCatalog,
				"getDataByDept": getDataByDept,
				"getDataItemByDept": getDataItemByDept,
				"getSystemByDept": getSystemByDept,
				"getResByDept": getResByDept,
				"getDataCountLast": getDataCountLast,
				"getThemeData": getThemeData,
				"getThemeRank": getThemeRank,
				"getInvokeNum": getInvokeNum
			}

			// 获取市级部门和乡镇统计
			function getDeptCatalog() {
				return $http.get(
					URL + '/combing/deptInfo'
				)
			}

			// 根据部门或乡镇获取信息资源目录个数
			function getDataByDept(params) {
				return $http.get(
					URL + '/combing/dirNum', {
						params: params
					}
				)
			}
			// 根据部门或乡镇获取目录数据项个数
			function getDataItemByDept(params) {
				return $http.get(
					URL + '/combing/itemNum', {
						params: params
					}
				)
			}
			// 根据部门或乡镇获取信息化系统个数
			function getSystemByDept(params) {
				return $http.get(
					URL + '/combing/systemNum', {
						params: params
					}
				)
			}
			// 根据部门或乡镇获取已汇聚数据资源个数
			function getResByDept(params) {
				return $http.get(
					URL + '/depDataInfo/dataSetNum', {
						params: params
					}
				)
			}

			// 获取已汇聚数据量和最近一周新增
			function getDataCountLast() {
				return $http.get(
					URL + '/depDataInfo/dataCountLast'
				)
			}

			// 获取数据专题数据
			function getThemeData() {
				return $http.get(
					URL + '/depDataInfo/subjectInfo'
				)
			}

			// 左下：主题数据排名
			function getThemeRank() {
				return $http.get(
					URL + '/depDataInfo/themeInfo'
				)
			}

			//应用调用数据集的次数
			function getInvokeNum() {
				return $http.get(
					URL + '/depDataInfo/invokeNumByApp'
				)
			}
		}
	])

	catalog.directive('wiservThemeRank', ['catalogService', '$interval',
		function(catalogService, $interval) {
			return {
				restrict: 'ACE',
				template: "<div id='themeRank' style='width:700px;height:500px'></div>",
				link: function(scope, element, attrs) {
					var chartInstance = echarts.init((element.find('#themeRank'))[0]);
					catalogService.getThemeRank().then(function(result) {
						// bar_bg.png 的base64
						var fillImg = 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAlCAYAAACONvPuAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA4RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpjMzk2MzljYy1hMzRkLTU3NDktODM4Yy05Y2U4YjYxOGYwNmQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QTJEQzNBQ0ZFOUUwMTFFN0I0ODhBMUJCODhGRTI5MTEiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QTJEQzNBQ0VFOUUwMTFFN0I0ODhBMUJCODhGRTI5MTEiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6ODc1NWY5ZjctZTA1Zi0yYzQ5LThmNTAtODQ0NjVkMGIzMWRhIiBzdFJlZjpkb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6YTUyODk2ODgtYTBhYy0xMWU2LWI3OWUtYTEyZjM3MGIwZTM1Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+GjlvQQAAAGpJREFUeNpi5Fx4n5WBgUEGiMWA+D8QfwHiOyA2E5AQhUqAACMQ8wKxJIgDkhRnwAQSQMzFxIAbsOKTZBiVHFaS//BJ/h8NIdpK/sYn+RAUPWjiH4D4K0jyBxA/AuJfQPwXiL9DNfwHCDAAj8ITzGkC6NsAAAAASUVORK5CYII=';
						// bar.png 的base64
						var spirit = 'image://data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAlCAYAAACONvPuAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA4RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpjMzk2MzljYy1hMzRkLTU3NDktODM4Yy05Y2U4YjYxOGYwNmQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NURGNjAzQUZFOUVBMTFFN0I1N0FFMDc2OTQ1RjMzOTAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NURGNjAzQUVFOUVBMTFFN0I1N0FFMDc2OTQ1RjMzOTAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6ODc1NWY5ZjctZTA1Zi0yYzQ5LThmNTAtODQ0NjVkMGIzMWRhIiBzdFJlZjpkb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6YTUyODk2ODgtYTBhYy0xMWU2LWI3OWUtYTEyZjM3MGIwZTM1Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+PWpIUgAAAG5JREFUeNpi5Fx4n5WBgUEaiE8CsRgQfwbiVCBexwQkeIF4M1SCAcqfCcSKIEkvINZhQAX8QDwNJMnIgB2IMzHgAaOSo5KjklSSfArE/7HI/QVJHgbiqegSQFwBM7YCiHcD8Qsg/gnEBUC8FyDAABIrEZcHojQvAAAAAElFTkSuQmCC'
						themeRankDataFormat(result);
						chartInstance.setOption(scope.option);


						var k = 0; // 定时器计时器
						var timeTicket = $interval(function() {
							var data0 = scope.option.series[0].data;
							var p2 = data0.splice(0, 1);
							var kl = scope.option.yAxis.data.splice(0, 1);

							var data_add = [];
							var name_add = [];
							for (var i = 0; i < 1; i++) {
								data0.push(scope.themeDataAll[(i + 1 * k + 6) % scope.data_length]);
								scope.option.yAxis.data.push(scope.themeNameAll[(i + 1 * k + 6) % scope.data_length]);
							}
							k++;
							chartInstance.setOption(scope.option);
						}, 3000);

						function themeRankDataFormat(result) {

								var res_data = _.sortBy(result.data.body, function(n) {
									return -n.num;
								});
								//var themeNameAll = ['交通运输', '住宿旅游', '餐饮美食', '医疗健康', '生活服务', '文娱娱乐', '消费购物', '房屋土地', '环境资源', '企业服务', '农业农村', '经济发展', '政法监察', '生活安全'];
								//var themeDataAll = [189, 182, 166, 127, 76, 64, 60, 52, 47, 45, 39, 31, 16, 5];
								scope.themeNameAll = _.map(res_data, 'themeName');
								scope.themeDataAll = _.map(res_data, 'num');

								var maxData = _.max(scope.themeDataAll) + 10; // 最大值
								scope.data_length = scope.themeNameAll.length;
								var themeNameTemp = angular.copy(scope.themeNameAll);
								var themeDataTemp = angular.copy(scope.themeDataAll);

								themeNameTemp.splice(6, scope.themeNameAll.length - 6);
								themeDataTemp.splice(6, themeDataTemp.length - 6);
								scope.option = {
									tooltip: {},
									xAxis: {
										max: maxData,
										splitLine: {
											show: false
										},
										axisTick: {
											show: false
										},
										axisLine: {
											show: false
										},
									},
									yAxis: {
										data: themeNameTemp,
										inverse: true,
										axisTick: {
											show: false
										},
										axisLine: {
											show: false
										},
										axisLabel: {
											margin: 25,
											textStyle: {
												color: '#09a1df',
												fontSize: 22
											}
										}
									},
									grid: {
										top: -15,
										left: 160,
										right: 160,
										bottom: 80
									},
									series: [{
										// 当前数据
										type: 'pictorialBar',
										symbol: spirit,
										symbolRepeat: 'fixed',
										symbolMargin: '50%',
										symbolClip: true,
										symbolSize: [5, 32],
										symbolBoundingData: maxData,
										data: themeDataTemp,

										z: 10
									}, {
										// 全部数据
										type: 'pictorialBar',
										label: {
											normal: {
												show: true,
												formatter: function(params) {
													return params.value;
												},
												position: 'right',
												offset: [20, 0],
												textStyle: {
													color: '#c8cbd7',
													fontSize: 22,
													align: 'left',
													fontFamily: 'digiFont'
												}
											}
										},
										animationDuration: 0,
										symbolRepeat: 'fixed',
										symbolMargin: '50%',
										symbol: fillImg,
										symbolSize: [5, 32],
										symbolBoundingData: maxData,
										data: themeDataTemp,
										z: 5
									}]
								};

						}

						// 定时器，每天更新数据
						$interval(function() {
							catalogService.getThemeRank().then(function(result) {
								themeRankDataFormat(result);
							})
						}, 86400000)
					})

				}
			}
		}
	]);

	catalog.directive('wiservChartTheme', ['catalogService', '$interval',
		function(catalogService, $interval) {
			return {
				restrict: 'ACE',
				template: '<canvas id="bubblecanvas" width="780" height="710" ></canvas>',
				link: function(scope, element, attrs) {
					// 数据专题
					var bubblecanvas = null;
					var groups = [];
					var index = 0;
					catalogService.getThemeData().then(function(res) {
						var bubblecanvas = new fabric.Canvas('bubblecanvas');

						if (res && res.data.body) {
							bubbleDataFormat(res);
						}

						scope.timer_theme = $interval(function() {
							animateCircle(groups[index % 6]);
							index++;
						}, 4500);

						function animateCircle(group) {
							fabric.util.animateColor('rgb(9,161,223)', 'rgb(200,235,36,1)', 1500, {
								onChange: function(value) {
									group.text_label.set('fill', value);
									group.text_number.set('fill', value);
									bubblecanvas.renderAll.bind(bubblecanvas);
								}
							})
							group.text_label.animate({
								'top': group.text_label.top - 18
							}, {
								duration: 1500,
								onChange: bubblecanvas.renderAll.bind(bubblecanvas),
								onComplete: function() {

								},
								easing: function(t, b, c, d) {
									return c * t / d + b;
								},
								//easing:fabric.util.ease.easeOutQuart
							});

							group.text_number.animate({
								'top': group.text_number.top - 15,
								'opacity': 1
							}, {
								duration: 1500,
								onChange: bubblecanvas.renderAll.bind(bubblecanvas),
								onComplete: function() {
									setTimeout(function() {
										group.text_label.animate({
											'top': group.text_label.top + 18
										}, {
											duration: 1500,
											onChange: bubblecanvas.renderAll.bind(bubblecanvas),
											easing: function(t, b, c, d) {
												return c * t / d + b;
											},
										});
										group.text_number.animate({
											'top': group.text_number.top + 15,
											'opacity': 0
										}, {
											duration: 1500,
											onChange: bubblecanvas.renderAll.bind(bubblecanvas),
											easing: function(t, b, c, d) {
												return c * t / d + b;
											},
										});

										fabric.util.animateColor('rgb(200,235,36,1)', 'rgb(9,161,223)', 1500, {
											onChange: function(value) {
												group.text_label.set('fill', value);
												group.text_number.set('fill', value);
											}
										});
										bubblecanvas.renderAll();
									}, 1500)


								},
								easing: function(t, b, c, d) {
									return c * t / d + b;
								}
							});

						}

						function circleGroupMaker(radius, c_left, c_top, text_label, text_number, fontSize, text_x, text_y) {
							var circle = new fabric.Circle({
								left: c_left,
								top: c_top,
								radius: radius,
								fill: '#FFF'
							});
							circle.setGradient('fill', {
								type: 'radial',
								x1: radius,
								y1: radius,
								x2: radius,
								y2: radius,
								r1: circle.width / 2,
								r2: 0,
								colorStops: {
									0: 'rgb(6,88,127)',
									0.05: 'rgba(16,52,75,.8)',
									1: 'rgba(16,52,75,1)'
								}
							});
							var text = new fabric.Text(text_label, {
								textAlign: 'center',
								left: text_x,
								top: text_y,
								fill: 'rgb(9,161,223)',
								fontSize: fontSize + 8,
								opacity: 1
							});
							var text_number = new fabric.Text(text_number, {
								left: text.left - 5,
								top: text.top + 45 + fontSize,
								fill: 'transparent',
								fontSize: fontSize,
								opacity: 0
							});
							var group = new fabric.Group([circle, text, text_number], {
								centeredRotation: true,
								selectable: false,
								hasControls: false,
								hoverCursor: 'pointer',
								originX: 'left'
							});
							group.text_label = text;
							group.text_number = text_number;
							return group;
						}

						function bubbleDataFormat(res) {
							var res_data = res.data.body;
							groups = [];
							var sub_str = "";
							_.forEach(res_data, function(n) {
								if (n.subjectName.length > 11) {
									sub_str = n.subjectName.substring(0, 6);
									n.subjectName = sub_str + "\n" + n.subjectName.substring(6, 15);
								} else {
									sub_str = n.subjectName.substring(0, 4);
									n.subjectName = sub_str + "\n" + n.subjectName.substring(4, 10);
								}

							})
							var group1 = circleGroupMaker(80, 50, 30, res_data[0].subjectName, ' 专题个数：' + res_data[0].num, 14, 86, 83);
							var group2 = circleGroupMaker(75, 240, 100, res_data[1].subjectName, '  专题个数：' + res_data[1].num, 13, 275, 153);
							var group3 = circleGroupMaker(103, 410, 0, res_data[2].subjectName, '   专题个数：' + res_data[2].num, 18, 452, 73);
							var group4 = circleGroupMaker(64, 120, 260, res_data[3].subjectName, '  专题个数：' + res_data[3].num, 10, 150, 305);
							var group5 = circleGroupMaker(86, 320, 260, res_data[4].subjectName, ' 专题个数：' + res_data[4].num, 16, 358, 315);
							var group6 = circleGroupMaker(72, 540, 200, res_data[5].subjectName, '        专题个数：' + res_data[5].num, 10, 552, 250);
							groups.push(group1);
							groups.push(group2);
							groups.push(group3);
							groups.push(group4);
							groups.push(group5);
							groups.push(group6);
							bubblecanvas.add(group1);
							bubblecanvas.add(group2);
							bubblecanvas.add(group3);
							bubblecanvas.add(group4);
							bubblecanvas.add(group5);
							bubblecanvas.add(group6);
						}

						// 定时器，每天更新数据
						$interval(function() {
							// 市级部门及乡镇统计数据
							catalogService.getThemeData().then(function(res) {

								bubblecanvas.clear();
								bubbleDataFormat(res);
								index = 0;
								$interval.cancel(scope.timer_theme);
								scope.timer_theme = $interval(function() {
									animateCircle(groups[index % 6]);
									index++;
								}, 4500);
							})
							// 
						}, 86400000)

					})



				}
			}
		}
	]);


})();