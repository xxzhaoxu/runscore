var auditOrderVM = new Vue({
	el : '#auditOrder',
	data : {
		global : GLOBAL,
		appealTypeDictItems : [],
		showWaitConfirmOrderFlag : true,
		waitConfirmOrders : [],
		selectedOrder : {},
		appealType : '',
		actualPayAmount : '',
		userSreenshotIds : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		headerVM.title = '审核订单';
		that.loadAppealTypeDictItem();
		that.loadPlatformOrder();
		window.setInterval(function() {
			that.loadPlatformOrder();
		}, 4500);

		$('.sreenshot').on('filebatchuploadsuccess', function(event, data) {
			that.userSreenshotIds = data.response.data.join(',');
			that.userStartAppealInner();
		});
	},
	methods : {

		/**
		 * 加载申诉类型字典项
		 */
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

		loadPlatformOrder : function() {
			var that = this;
			that.$http.get('/merchantOrder/findMyWaitConfirmOrder').then(function(res) {
				that.waitConfirmOrders = res.body.data;
			});
		},

		showAppealDetailsPage : function(id) {
			window.location.href = '/appeal-details?orderId=' + id;
		},

		confirmToPaid : function(orderId) {
			var that = this;
			layer.confirm('确认收到款了吗?', {
				title : '提示',
				btn : [ '确认', '返回' ]
			}, function(index) {
				layer.close(index);
				that.confirmToPaidInner(orderId);
			}, function(index) {
				layer.close(index);
			});
		},

		confirmToPaidInner : function(orderId) {
			var that = this;
			that.$http.get('/merchantOrder/userConfirmToPaid', {
				params : {
					orderId : orderId
				}
			}).then(function(res) {
				layer.alert('操作成功', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.loadPlatformOrder();
			});
		},

		showAppealPage : function(order) {
			this.selectedOrder = order;
			this.appealType = '';
			this.actualPayAmount = '';
			this.userSreenshotIds = '';
			headerVM.title = '申诉';
			this.showWaitConfirmOrderFlag = false;
			this.initFileUploadWidget();
		},

		showWaitConfirmOrderPage : function() {
			headerVM.title = '审核订单';
			this.showWaitConfirmOrderFlag = true;
			this.loadPlatformOrder();
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

		userStartAppeal : function() {
			var that = this;
			if (that.appealType == null || that.appealType == '') {
				layer.alert('请选择申诉类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.appealType == '2' || that.appealType == '4') {
				if (that.actualPayAmount == null || that.actualPayAmount == '') {
					layer.alert('请输入实际支付金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (that.appealType == '2' && that.selectedOrder.gatheringAmount <= that.actualPayAmount) {
					layer.alert('实际支付金额须小于收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (that.appealType == '4' && that.selectedOrder.gatheringAmount >= that.actualPayAmount) {
					layer.alert('实际支付金额须大于收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
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
				that.userSreenshotIds = '';
				that.userStartAppealInner();
			}
		},

		userStartAppealInner : function() {
			var that = this;
			that.$http.post('/appeal/userStartAppeal', {
				appealType : that.appealType,
				actualPayAmount : that.actualPayAmount,
				userSreenshotIds : that.userSreenshotIds,
				merchantOrderId : that.selectedOrder.id
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('发起申诉成功', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.showWaitConfirmOrderPage();
			});
		}
	}
});