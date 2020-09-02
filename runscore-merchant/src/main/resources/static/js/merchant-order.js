var merchantOrderVM = new Vue({
	el : '#merchant-order',
	data : {
		global : GLOBAL,
		orderNo : '',
		platformName : '',
		gatheringChannelCode : '',
		gatheringChannelDictItems : [],
		orderState : '',
		platformOrderStateDictItems : [],
		receiverUserName : '',
		submitStartTime : dayjs().format('YYYY-MM-DD'),
		submitEndTime : dayjs().format('YYYY-MM-DD'),
		payNoticeState : '',
		payNoticeStateDictItems : [],
		showMerchantRecordFlag : true,

		defaultNofityUrl : '',
		defaultReturnUrl : '',
		showAddOrderFlag : false,
		newOrders : [],
		notifyUrlWithAddOrder : '',
		returnUrlWithAddOrder : '',

		showSplitOrderFlag : false,
		splitAmount : '',
		newOrderIndex : '',
		splitOrderNumber : '',
		splitOrderRule : '2',

		showOrderDetailsFlag : false,
		selectedOrderDetails : {},

		showStartAppealFlag : false,
		appealTypeDictItems : [],
		appealType : '',
		actualPayAmount : '',
		merchantSreenshotIds : ''
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
		var that = this;
		that.loadGatheringChannelDictItem();
		that.loadMerchantOrderStateDictItem();
		that.loadPayNoticeStateDictItem();
		that.loadAppealTypeDictItem();
		that.loadDefaultNotifyUrl();
		that.initTable();

		$('.sreenshot').on('filebatchuploadsuccess', function(event, data) {
			that.merchantSreenshotIds = data.response.data.join(',');
			that.merchantStartAppealInner();
		});
	},
	methods : {

		/**
		 * 加载收款通道字典项
		 */
		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel').then(function(res) {
				this.gatheringChannelDictItems = res.body.data;
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
				this.platformOrderStateDictItems = res.body.data;
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

		loadAppealTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealType'
				}
			}).then(function(res) {
				this.appealTypeDictItems = res.body.data;
			});
		},

		loadDefaultNotifyUrl : function() {
			var that = this;
			that.$http.get('/merchant/getMerchantInfo').then(function(res) {
				that.defaultNotifyUrl = res.body.data.notifyUrl;
				that.defaultReturnUrl = res.body.data.returnUrl;
			});
		},

		initTable : function() {
			var that = this;
			$('.platform-order-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/merchantOrder/findMerchantOrderByPage',
				pagination : true,
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
						platformName : that.platformName,
						orderState : that.orderState,
						gatheringChannelCode : that.gatheringChannelCode,
						receiverUserName : that.receiverUserName,
						submitStartTime : that.submitStartTime,
						submitEndTime : that.submitEndTime,
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
					var html = template('platform-order-detail', {
						merchantOrderInfo : row
					});
					return html;
				},
				columns : [ {
					title : '订单号/商户订单号',
					formatter : function(value, row, index, field) {
						var text = row.orderNo + '/' + row.payInfo.orderNo;
						return text;
					}
				}, {
					field : 'orderStateName',
					title : '订单状态'
				}, {
					title : '收款通道/收款金额/费率',
					formatter : function(value, row, index, field) {
						var text = row.gatheringChannelName + '/' + row.gatheringAmount + that.global.systemSetting.currencyUnit + '/' + row.rate + '%';
						return text;
					}
				}, {
					title : '接单人/接单时间',
					formatter : function(value, row, index, field) {
						if (row.receiverUserName == null) {
							return;
						}
						var text = row.receiverUserName + '/' + row.receivedTime;
						return text;
					}
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

		showAddOrderModal : function() {
			this.showAddOrderFlag = true;
			this.newOrders = [];
			this.notifyUrlWithAddOrder = this.defaultNotifyUrl;
			this.returnUrlWithAddOrder = this.defaultReturnUrl;
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
					layer.alert('请选择收款渠道', {
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
		},

		cancelOrder : function(id) {
			var that = this;
			layer.confirm('确定要取消订单吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchantOrder/merchantCancelOrder', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.showAddOrderFlag = false;
					that.refreshTable();
				});
			});
		},

		showMerchantRecordPage : function() {
			this.showMerchantRecordFlag = true;
			this.showOrderDetailsFlag = false;
			this.showStartAppealFlag = false;
			this.refreshTable();
		},

		showStartAppealPage : function(orderId) {
			var that = this;
			that.$http.get('/merchantOrder/findMerchantOrderDetailsById', {
				params : {
					orderId : orderId
				}
			}).then(function(res) {
				that.selectedOrderDetails = res.body.data;
				that.showMerchantRecordFlag = false;
				that.showOrderDetailsFlag = false;
				that.showStartAppealFlag = true;
				that.appealType = '';
				that.actualPayAmount = '';
				that.initFileUploadWidget();
			});
		},

		initFileUploadWidget : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.sreenshot').fileinput('destroy').fileinput({
				uploadAsync : false,
				browseOnZoneClick : true,
				showBrowse : false,
				showCaption : false,
				showClose : true,
				showRemove : false,
				showUpload : false,
				dropZoneTitle : '点击选择图片',
				dropZoneClickTitle : '',
				layoutTemplates : {
					footer : ''
				},
				maxFileCount : 2,
				uploadUrl : '/storage/uploadPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		merchantStartAppeal : function() {
			var that = this;
			if (that.appealType == null || that.appealType == '') {
				layer.alert('请选择申诉类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.appealType === '2' || that.appealType === '4') {
				if (that.actualPayAmount === null || that.actualPayAmount === '') {
					layer.alert('请输入实际支付金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (that.appealType === '2' && that.selectedOrderDetails.gatheringAmount <= that.actualPayAmount) {
					layer.alert('实际支付金额须小于收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (that.appealType === '4' && that.selectedOrderDetails.gatheringAmount >= that.actualPayAmount) {
					layer.alert('实际支付金额须大于收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if (that.appealType == '2' || that.appealType == '3' || that.appealType == '4') {
				var filesCount = $('.sreenshot').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请上传截图', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				$('.sreenshot').fileinput('upload');
			} else {
				that.merchantSreenshotIds = '';
				that.merchantStartAppealInner();
			}
		},

		merchantStartAppealInner : function() {
			var that = this;
			that.$http.post('/appeal/merchantStartAppeal', {
				appealType : that.appealType,
				actualPayAmount : that.actualPayAmount,
				merchantSreenshotIds : that.merchantSreenshotIds,
				merchantOrderId : that.selectedOrderDetails.id
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showMerchantRecordPage();
			});
		}
	}
});