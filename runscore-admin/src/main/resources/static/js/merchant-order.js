var platformOrderVM = new Vue({
	el : '#platform-order',
	data : {
		global : GLOBAL,
		orderNo : '',
		merchantName : '',
		merchantOrderNo : '',
		gatheringChannelCode : '',
		gatheringChannelDictItems : [],
		orderState : '',
		merchantOrderStateDictItems : [],
		receiverUserName : '',
		submitStartTime : dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
		submitEndTime : dayjs().format('YYYY-MM-DD'),
		payNoticeState : '',
		payNoticeStateDictItems : [],

		showAddOrderFlag : false,
		merchantDictItems : [],
		newOrders : [],
		selectedMerchant : '',
		notifyUrlWithAddOrder : '',
		returnUrlWithAddOrder : '',

		showSplitOrderFlag : false,
		splitAmount : '',
		newOrderIndex : '',
		splitOrderNumber : '',
		splitOrderRule : '2'
	},
	computed : {
		newOrderTotalAmount : function() {
			var totalAmount = 0;
			for (var i = 0; i < this.newOrders.length; i++) {
				var newOrder = this.newOrders[i];
				if (newOrder.gatheringAmount != null && newOrder.gatheringAmount != '') {
					totalAmount += newOrder.gatheringAmount;
				}
			}
			return numberFormat(totalAmount);
		},

	},
	created : function() {
	},
	mounted : function() {
		var merchantName = getQueryString('merchantName');
		if (merchantName != null && merchantName != '') {
			this.merchantName = decodeURI(merchantName);
		}
		var orderState = getQueryString('orderState');
		if (orderState != null && orderState != '') {
			this.orderState = orderState;
		}
		var submitStartTime = getQueryString('submitStartTime');
		if (submitStartTime != null && submitStartTime != '') {
			if (submitStartTime == 'all') {
				this.submitStartTime = '';
			} else {
				this.submitStartTime = submitStartTime;
			}
		}
		var submitEndTime = getQueryString('submitEndTime');
		if (submitEndTime != null && submitEndTime != '') {
			if (submitEndTime == 'all') {
				this.submitEndTime = '';
			} else {
				this.submitEndTime = submitEndTime;
			}
		}

		this.loadGatheringChannelDictItem();
		this.loadMerchantOrderStateDictItem();
		this.loadPayNoticeStateDictItem();
		this.initTable();
	},
	methods : {

		/**
		 * 加载收款通道字典项
		 */
		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel').then(function(res) {
				that.gatheringChannelDictItems = res.body.data;
			});
		},

		/**
		 * 加载商户订单状态字典项
		 */
		loadMerchantOrderStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'merchantOrderState'
				}
			}).then(function(res) {
				this.merchantOrderStateDictItems = res.body.data;
			});
		},

		loadPayNoticeStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'merchantOrderPayNoticeState'
				}
			}).then(function(res) {
				this.payNoticeStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.platform-order-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/merchantOrder/findMerchantOrderByPage',
				pagination : true,
				showFooter:true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						orderNo : that.orderNo,
						merchantName : that.merchantName,
						merchantOrderNo : that.merchantOrderNo,
						orderState : that.orderState,
						gatheringChannelCode : that.gatheringChannelCode,
						receiverUserName : that.receiverUserName,
						submitStartTime : that.submitStartTime,
						submitEndTime : that.submitEndTime,
						payNoticeState : that.payNoticeState
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				detailView : true,
				detailFormatter : function(index, row, element) {
					var html = template('merchant-order-detail', {
						merchantOrderInfo : row
					});
					return html;
				},
				columns : [ {
					title : '订单号/商户/商户订单号',
					formatter : function(value, row, index, field) {
						var text = row.orderNo + ' / ' + row.platformName + ' / ' + row.payInfo.orderNo;
						return text;
					}
				}, {
					field : 'orderStateName',
					title : '订单状态',
					formatter : function(value, row, index, field) {
						if (row.orderStateName == '已支付') {
							return  '<span style="color: green;">已支付</span>';
						}else if(row.orderStateName == '未确认超时取消'){
							return  '<span style="color: red;">未确认超时取消</span>';
						}else{
							return row.orderStateName;
						}
					},
					footerFormatter: function (){
						return '金额汇总：'
					}
				}, {
					title : '通道/金额/费率',
					formatter : function(value, row, index, field) {
						var text = row.gatheringChannelName + '/' + row.gatheringAmount + that.global.systemSetting.currencyUnit + '/' + row.rate + '%';
						return text;
					},
					//计算此列的值
	                footerFormatter: function(rows){
	                    var sum = 0;
	                    for (var i=0;i<rows.length;i++) {
	                        sum += rows[i].gatheringAmount
	                    }
	                    return sum + " "+ that.global.systemSetting.currencyUnit;
	                }
				}, {
					title : '奖励金/返点',
					formatter : function(value, row, index, field) {
						if (row.bounty != null) {
							text = row.bounty + that.global.systemSetting.currencyUnit + '/' + row.rebate + '%';
							return text;
						}
					}
				}, {
					title : '接单人/收款人/接单时间/邀请人',
					formatter : function(value, row, index, field) {
						if (row.receiverUserName == null) {
							return;
						}
						var text = row.receiverUserName + '<span style="color: red;">/</span>' + row.payee + '<span style="color: red;">/</span>' + row.receivedTime + '<span style="color: red;">/</span>' + row.inviterUserName;
						if(row.gatheringChannelName == "银行卡"){
						   text = row.receiverUserName + '<span style="color: red;">/</span>' + row.accountHolder + '<span style="color: red;">/</span>' + row.receivedTime + '<span style="color: red;">/</span>' + row.inviterUserName;
						}
						return text;
					}
				}, {
					field : 'ip',
					title : 'IP地址'
				}, {
					field : 'submitTime',
					title : '提交时间'
				}, {
					title : '通知状态',
					formatter : function(value, row, index, field) {
						return row.payInfo.noticeStateName;
					}
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						var btns = [];
						if (row.orderState == '1') {
							btns.push('<button type="button" class="cancel-order-btn btn btn-outline-danger btn-sm">取消订单</button>');
						}
						if (row.orderState == '4' && (row.payInfo.noticeState == '1' || row.payInfo.noticeState == '3')) {
							btns.push('<button type="button" class="resend-notice-btn btn btn-outline-info btn-sm">重发通知</button>');
						}
						if (btns.length > 0) {
							return btns.join('');
						}
					},
					events : {
						'click .cancel-order-btn' : function(event, value, row, index) {
							that.cancelOrder(row.id);
						},
						'click .resend-notice-btn' : function(event, value, row, index) {
							that.resendNotice(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.platform-order-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		confirmToPaidWithCancelOrderRefund : function(orderId) {
			var that = this;
			layer.confirm('确定更改为已支付状态吗', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/confirmToPaidWithCancelOrderRefund', {
					params : {
						orderId : orderId
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		confirmToPaidWithUnconfirmedAutoFreeze : function(orderId) {
			var that = this;
			layer.confirm('确定更改为已支付状态吗', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/confirmToPaidWithUnconfirmedAutoFreeze', {
					params : {
						orderId : orderId
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},
		confirmToOrderFreezeRecord : function(orderId) {
			var that = this;
			layer.confirm('确定手动取消冻结余额吗', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/confirmToOrderFreezeRecord', {
					params : {
						orderId : orderId
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		confirmToPaid : function(userAccountId, orderId) {
			var that = this;
			layer.confirm('确认收到款了吗', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/confirmToPaid', {
					params : {
						userAccountId : userAccountId,
						orderId : orderId
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		cancelOrderRefund : function(id) {
			var that = this;
			layer.confirm('确定要取消订单退款吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/cancelOrderRefund', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		forceCancelOrder : function(id) {
			var that = this;
			layer.confirm('确定要强制取消订单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/forceCancelOrder', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showSupplementOrderModal : function(id, gatheringAmount) {
			var that = this;
			layer.prompt({
				title : '请输入金额并按确定进行补单',
				formType : 0,
				value : gatheringAmount,
			}, function(text, index) {
				layer.close(index);
				that.supplementOrder(id, text);
			});
		},

		supplementOrder : function(id, gatheringAmount) {
			var that = this;
			if (gatheringAmount == null || gatheringAmount == '') {
				layer.alert('清输入金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchantOrder/supplementOrder', {
				id : id,
				gatheringAmount : gatheringAmount
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.refreshTable();
			});
		},

		cancelOrder : function(id) {
			var that = this;
			layer.confirm('确定要取消订单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/cancelOrder', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showNoteModal : function(id) {
			var that = this;
			layer.prompt({
				title : '请输入备注',
				formType : 2
			}, function(text, index) {
				layer.close(index);
				that.updateNote(id, text);
			});
		},

		updateNote : function(id, note) {
			var that = this;
			that.$http.post('/merchantOrder/updateNote', {
				id : id,
				note : note
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.refreshTable();
			});
		},

		resendNotice : function(id) {
			var that = this;
			layer.load(0, {
				time : 8000,
				shade : [ 0.1, '#fff' ]
			});
			that.$http.get('/merchantOrder/resendNotice', {
				params : {
					id : id
				}
			}).then(function(res) {
				layer.closeAll('loading');
				that.refreshTable();
				var result = res.body.data;
				if (result == null || result == '') {
					result = '无内容';
				}
				layer.open({
					type : 1,
					anim : 2,
					shadeClose : true,
					area : [ '420px', '240px' ],
					content : '<div style="padding: 10px;">' + '<p>返回内容</p>' + '<div style="color: red;">' + result + '</div>' + '</div>'
				});
			});
		},

		loadMerchantDictItem : function() {
			var that = this;
			that.$http.get('/merchant/findAllMerchant').then(function(res) {
				that.merchantDictItems = res.body.data;
			});
		},

		setDefaultNotifyUrl : function() {
			if (this.selectedMerchant == null || this.selectedMerchant == '') {
				return;
			}
			this.notifyUrlWithAddOrder = this.selectedMerchant.notifyUrl;
			this.returnUrlWithAddOrder = this.selectedMerchant.returnUrl;
		},

		showAddOrderModal : function() {
			this.showAddOrderFlag = true;
			this.loadMerchantDictItem();
			this.newOrders = [];
			this.selectedMerchant = '';
			this.notifyUrlWithAddOrder = '';
			this.returnUrlWithAddOrder = '';
			this.addOrder();
		},

		addOrder : function() {
			this.newOrders.push({
				gatheringChannelCode : '',
				gatheringAmount : '',
				orderNo : '',
				attch : '',
				specifiedReceivedAccountId : '',
				publishWay : '1',
				publishTime : dayjs().format('YYYY-MM-DDTHH:mm'),
			});
		},

		showSplitOrderModal : function(newOrder, index) {
			if (newOrder.gatheringAmount === null || newOrder.gatheringAmount === '') {
				layer.alert('收款金额为空,无法拆单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (newOrder.gatheringAmount <= 0) {
				layer.alert('收款金额必须大于0', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			this.showSplitOrderFlag = true;
			this.splitAmount = newOrder.gatheringAmount;
			this.newOrderIndex = index;
			this.splitOrderNumber = '';
			this.splitOrderRule = '2';
		},

		splitOrder : function() {
			if (this.splitOrderNumber === null || this.splitOrderNumber === '') {
				layer.alert('请输入单数', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.splitOrderNumber <= 0) {
				layer.alert('单数必须大于0', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (!(/^\+?[1-9][0-9]*$/.test(this.splitOrderNumber))) {
				layer.alert('单数必须为正整数', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			var splitOrderAmounts = [];
			if (this.splitOrderRule == '1') {
				var avgAmount = numberFormat(this.splitAmount / this.splitOrderNumber);
				for (var i = 0; i < this.splitOrderNumber; i++) {
					splitOrderAmounts.push(avgAmount);
				}
			} else {
				splitOrderAmounts = this.randomSplitAmount(this.splitAmount, this.splitOrderNumber);
			}
			var tmpNewOrders = [];
			for (var i = 0; i < this.newOrderIndex; i++) {
				tmpNewOrders.push(this.newOrders[i]);
			}
			for (var i = 0; i < splitOrderAmounts.length; i++) {
				var tmpNewOrder = JSON.parse(JSON.stringify(this.newOrders[this.newOrderIndex]));
				tmpNewOrder.gatheringAmount = splitOrderAmounts[i];
				tmpNewOrders.push(tmpNewOrder);
			}
			for (var i = this.newOrderIndex + 1; i < this.newOrders.length; i++) {
				tmpNewOrders.push(this.newOrders[i]);
			}
			this.newOrders = tmpNewOrders;
			this.showSplitOrderFlag = false;

		},

		randomSplitAmount : function(totalAmount, totalOrderNumber) {
			var remainAmount = +totalAmount;
			var remainOrderNumber = +totalOrderNumber;
			var arr = [];
			while (remainOrderNumber > 0) {
				var amount = numberFormat(this.randomSplitAmountInner(remainAmount, remainOrderNumber));
				remainAmount = remainAmount - amount;
				remainOrderNumber--;
				arr.push(amount);
			}
			return arr;
		},

		randomSplitAmountInner : function(remainAmount, remainOrderNumber) {
			if (remainOrderNumber === 1) {
				return remainAmount.toFixed(2);
			}
			var max = ((remainAmount / remainOrderNumber) * 2 - 0.01).toFixed(2);
			var min = 0.01;
			var range = max - min;
			var rand = Math.random();
			var amount = Math.round(rand * range);
			return amount;
		},

		batchStartOrder : function() {
			var that = this;
			var newOrders = that.newOrders;
			if (newOrders.length == 0) {
				layer.alert('新增的订单不能为空', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			for (var i = 0; i < newOrders.length; i++) {
				var newOrder = newOrders[i];
				if (newOrder.gatheringChannelCode === null || newOrder.gatheringChannelCode === '') {
					layer.alert('请选择收款通道', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (newOrder.gatheringAmount === null || newOrder.gatheringAmount === '') {
					layer.alert('请输入收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (newOrder.orderNo === null || newOrder.orderNo === '') {
					layer.alert('请输入商户订单号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (newOrder.publishWay === '2') {
					if (newOrder.publishTime === null || newOrder.publishTime === '') {
						layer.alert('请输入发单时间', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
				}
			}
			if (that.selectedMerchant === null || that.selectedMerchant === '') {
				layer.alert('请选择商户', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.notifyUrlWithAddOrder === null || that.notifyUrlWithAddOrder === '') {
				layer.alert('请输入异步通知地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var batchStartOrderParams = [];
			for (var i = 0; i < newOrders.length; i++) {
				var newOrder = newOrders[i];
				batchStartOrderParams.push({
					gatheringChannelCode : newOrder.gatheringChannelCode,
					gatheringAmount : newOrder.gatheringAmount,
					orderNo : newOrder.orderNo,
					attch : newOrder.attch,
					specifiedReceivedAccountId : newOrder.specifiedReceivedAccountId,
					publishWay : newOrder.publishWay,
					publishTime : newOrder.publishWay == '1' ? null : dayjs(newOrder.publishTime).format('YYYY-MM-DD HH:mm:ss'),
					merchantNum : that.selectedMerchant.merchantNum,
					notifyUrl : that.notifyUrlWithAddOrder,
					returnUrl : that.returnUrlWithAddOrder,
				});
			}

			that.$http.post('/merchantOrder/startOrder', batchStartOrderParams).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showAddOrderFlag = false;
				that.refreshTable();
			});
		}

	}
});