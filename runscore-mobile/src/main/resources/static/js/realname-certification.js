var gatheringCodeVM = new Vue({
	el : '#realnameCertification',
	data : {
		global : GLOBAL,
		auditGatheringCode : false,
		cardWithStorageId : '',
		cardIsStorageId : '',
		cardTheStorageId : '',
		unfixedGatheringCodeReceiveOrder : false,
		editGatheringCode : {
			state : ''
		}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		headerVM.title = '实名认证';
		headerVM.showBackFlag = true;
		that.getUserAccountInfo();
		$('.realname-certification-withpic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.cardWithStorageId = data.response.data.join(',');
		});
		$('.realname-certification-zpic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.cardIsStorageId = data.response.data.join(',');
		});
		$('.realname-certification-fpic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.cardTheStorageId = data.response.data.join(',');
			//that.addOrUpdateGatheringCodeInner();
		});
	},
	methods : {
		/**
		 * 获取用户账号信息
		 */
		getUserAccountInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getUserAccountInfo').then(function(res) {
				if (res.body.data != null) {
					that.cardWithStorageId = res.body.data.cardWithStorageId;
					that.cardIsStorageId = res.body.data.cardIsStorageId;
					that.cardTheStorageId = res.body.data.cardTheStorageId;
					that.initFileUploadWithpic(that.cardWithStorageId);
					that.initFileUploadZpic(that.cardIsStorageId);
					that.initFileUploadFpic(that.cardTheStorageId);
				}
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

		initFileUploadZpic : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.realname-certification-zpic').fileinput('destroy').fileinput({
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
				uploadUrl : '/storage/uploadCardPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},
		initFileUploadFpic : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.realname-certification-fpic').fileinput('destroy').fileinput({
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
				uploadUrl : '/storage/uploadCardPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},
		initFileUploadWithpic : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.realname-certification-withpic').fileinput('destroy').fileinput({
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
				uploadUrl : '/storage/uploadCardPic',
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		addOrUpdateGatheringCode : function() {
			var that = this;
			if ($('.realname-certification-withpic').fileinput('getPreview').content.length == 0) {
				var filesCount = $('.realname-certification-withpic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的手持身份证', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if ($('.realname-certification-zpic').fileinput('getPreview').content.length == 0) {
				var filesCount = $('.realname-certification-zpic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的身份证正面', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}
			if ($('.realname-certification-fpic').fileinput('getPreview').content.length == 0) {
				var filesCount = $('.realname-certification-fpic').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请选择要上传的身份证反面', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}

			/*if ($('.realname-certification-withpic').fileinput('getPreview').content.length != 0 &&
					$('.realname-certification-zpic').fileinput('getPreview').content.length != 0 &&
					$('.realname-certification-fpic').fileinput('getPreview').content.length != 0) {
				that.addOrUpdateGatheringCodeInner();
			} else {
				$('.realname-certification-withpic').fileinput('upload');
				$('.realname-certification-zpic').fileinput('upload');
				$('.realname-certification-fpic').fileinput('upload');
			}*/
			$('.realname-certification-withpic').fileinput('upload');
			$('.realname-certification-zpic').fileinput('upload');
			$('.realname-certification-fpic').fileinput('upload');
		},

		addOrUpdateGatheringCodeInner : function() {
			var that = this;
			that.$http.post('/userAccount/updateUserCard', that.editGatheringCode).then(function(res) {
				var msg = '操作成功!';
				layer.alert(msg, {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		},

	}
});