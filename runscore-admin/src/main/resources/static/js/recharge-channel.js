var rechargeChannelVM = new Vue({
	el : '#recharge-channel',
	data : {
		payTypeDicts : [],
		tabWithPayType : {},
		payChannels : [],
		showPayChannelFlag : true,

		editPayChannel : {},
		addOrUpdatePayChannelFlag : false,
		payChannelActionTitle : '',

		payCategoryDictItems : [],
		payTypes : [],
		showPayTypeFlag : false,

		editPayType : {},
		addOrUpdatePayTypeFlag : false,
		payTypeActionTitle : '',

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadPayTypeDict();
		that.loadPayChannel();
		$('.gathering-code-pic').on('fileuploaded', function(event, data, previewId, index) {
			that.editPayChannel.gatheringCodeStorageId = data.response.data.join(',');
			that.addOrUpdatePayChannelInner();
		});
	},
	methods : {

		loadPayTypeDict : function() {
			var that = this;
			that.$http.get('/recharge/findAllPayType', {}).then(function(res) {
				that.payTypeDicts = res.body.data;
				if (that.payTypeDicts.length > 0) {
					that.tabWithPayType = that.payTypeDicts[0];
				} else {
					that.tabWithPayType = {};
				}
			});
		},

		loadPayChannel : function() {
			var that = this;
			that.$http.get('/recharge/findAllPayChannel').then(function(res) {
				that.payChannels = res.body.data;
			});
		},

		viewImage : function(imagePath) {
			var image = new Image();
			image.src = imagePath;
			var viewer = new Viewer(image, {
				hidden : function() {
					viewer.destroy();
				},
			});
			viewer.show();
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
			$('.gathering-code-pic').fileinput('destroy').fileinput({
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
				maxFileCount : 1,
				uploadUrl : '/storage/uploadPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		showAddPayChannelModal : function() {
			this.addOrUpdatePayChannelFlag = true;
			this.payChannelActionTitle = '新增通道';
			this.editPayChannel = {
				payTypeId : this.tabWithPayType.id,
				channelCode : '',
				channelName : '',
				orderNo : '',
				enabled : true,
				bankName : '',
				accountHolder : '',
				bankCardAccount : '',

				payPlatformCode : '',
				payPlatformName : '',
				payPlatformChannelCode : '',

				virtualWalletAddr : '',

				payee : '',
				gatheringCodeStorageId : '',
			};
			this.initFileUploadWidget();
		},

		showEditPayChannelModal : function(id) {
			var that = this;
			that.$http.get('/recharge/findPayChannelById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editPayChannel = res.body.data;
				that.initFileUploadWidget(that.editPayChannel.gatheringCodeStorageId);
				that.addOrUpdatePayChannelFlag = true;
				that.payChannelActionTitle = '编辑通道';
			});
		},

		addOrUpdatePayChannel : function() {
			var that = this;
			var editPayChannel = that.editPayChannel;
			if (editPayChannel.channelCode == null || editPayChannel.channelCode == '') {
				layer.alert('请输入通道code', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editPayChannel.channelName == null || editPayChannel.channelName == '') {
				layer.alert('请输入通道名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editPayChannel.orderNo == null || editPayChannel.orderNo == '') {
				layer.alert('请选择排序号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.tabWithPayType.payCategory == 'bankCard') {
				if (editPayChannel.bankName == null || editPayChannel.bankName == '') {
					layer.alert('请输入收款银行', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editPayChannel.accountHolder == null || editPayChannel.accountHolder == '') {
					layer.alert('请输入收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editPayChannel.bankCardAccount == null || editPayChannel.bankCardAccount == '') {
					layer.alert('请输入卡号', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if (that.tabWithPayType.payCategory == 'thirdPartyPay') {
				if (editPayChannel.payPlatformCode == null || editPayChannel.payPlatformCode == '') {
					layer.alert('请输入支付平台code', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editPayChannel.payPlatformName == null || editPayChannel.payPlatformName == '') {
					layer.alert('请输入支付平台名称', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (editPayChannel.payPlatformChannelCode == null || editPayChannel.payPlatformChannelCode == '') {
					layer.alert('请输入对应通道code', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if (that.tabWithPayType.payCategory == 'virtualWallet') {
				if (editPayChannel.virtualWalletAddr == null || editPayChannel.virtualWalletAddr == '') {
					layer.alert('请输入钱包地址', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if (that.tabWithPayType.payCategory == 'gatheringCode') {
				if (editPayChannel.payee == null || editPayChannel.payee == '') {
					layer.alert('请输入收款人', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if (that.tabWithPayType.payCategory == 'gatheringCode') {
				if ($('.gathering-code-pic').fileinput('getPreview').content.length != 0) {
					that.addOrUpdatePayChannelInner();
				} else {
					var filesCount = $('.gathering-code-pic').fileinput('getFilesCount');
					if (filesCount == 0) {
						layer.alert('请选择要上传的收款码', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
					$('.gathering-code-pic').fileinput('upload');
				}
			} else {
				that.addOrUpdatePayChannelInner();
			}

		},

		addOrUpdatePayChannelInner : function() {
			var that = this;
			that.$http.post('/recharge/addOrUpdatePayChannel', that.editPayChannel).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdatePayChannelFlag = false;
				that.loadPayChannel();
			});
		},

		delPayChannel : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/recharge/delPayChannelById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.loadPayChannel();
				});
			});
		},

		toPayTypePage : function() {
			var that = this;
			this.loadPayCategoryDictItem();
			that.$http.get('/recharge/findAllPayType', {}).then(function(res) {
				that.payTypes = res.body.data;
				that.showPayChannelFlag = false;
				that.showPayTypeFlag = true;
			});
		},

		backToPayChannelPage : function() {
			this.showPayTypeFlag = false;
			this.showPayChannelFlag = true;
			this.loadPayTypeDict();
			this.loadPayChannel();
		},

		loadPayCategoryDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'payCategory'
				}
			}).then(function(res) {
				this.payCategoryDictItems = res.body.data;
			});
		},

		showAddPayTypeModal : function() {
			this.addOrUpdatePayTypeFlag = true;
			this.payTypeActionTitle = '新增支付类型';
			this.editPayType = {
				type : '',
				name : '',
				payCategory : '',
				orderNo : '',
				enabled : true
			};
		},

		showEditPayTypeModal : function(id) {
			var that = this;
			that.$http.get('/recharge/findPayTypeById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editPayType = res.body.data;
				this.addOrUpdatePayTypeFlag = true;
				this.payTypeActionTitle = '编辑支付类型';
			});
		},

		addOrUpdatePayType : function() {
			var that = this;
			var editPayType = that.editPayType;
			if (editPayType.type == null || editPayType.type == '') {
				layer.alert('请输入支付类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editPayType.name == null || editPayType.name == '') {
				layer.alert('请输入名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editPayType.payCategory == null || editPayType.payCategory == '') {
				layer.alert('请选择所属类别', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editPayType.orderNo == null || editPayType.orderNo == '') {
				layer.alert('请输入排序号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/recharge/addOrUpdatePayType', editPayType).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdatePayTypeFlag = false;
				that.toPayTypePage();
			});
		},

		delPayType : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/recharge/delPayTypeById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.toPayTypePage();
				});
			});
		}

	}
});