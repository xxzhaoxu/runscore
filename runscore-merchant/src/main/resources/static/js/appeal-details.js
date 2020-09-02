var appealDetailsVM = new Vue({
	el : '#appeal-details',
	data : {
		global : GLOBAL,
		selectAppeal : {},
		showUploadPicModalFlag : false,
		merchantSreenshotIds : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		var id = getQueryString('id');
		this.loadAppealDetails(id);
		$('.upload-sreenshot').on('filebatchuploadsuccess', function(event, data) {
			that.merchantSreenshotIds = data.response.data.join(',');
			that.uploadSreenshotInner();
		});
	},
	methods : {
		loadAppealDetails : function(appealId) {
			var that = this;
			that.$http.get('/appeal/findAppealById', {
				params : {
					appealId : appealId
				}
			}).then(function(res) {
				this.selectAppeal = res.body.data;
			});
		},

		showUploadPicModal : function() {
			this.showUploadPicModalFlag = true;
			this.initFileUploadWidget();
		},

		initFileUploadWidget : function() {
			var initialPreview = [];
			var initialPreviewConfig = [];
			$('.upload-sreenshot').fileinput('destroy').fileinput({
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

		uploadSreenshot : function() {
			var filesCount = $('.upload-sreenshot').fileinput('getFilesCount');
			if (filesCount == 0) {
				layer.alert('请上传截图', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			$('.upload-sreenshot').fileinput('upload');
		},

		uploadSreenshotInner : function() {
			var that = this;
			that.$http.get('/appeal/merchantUploadSreenshot', {
				params : {
					appealId : that.selectAppeal.id,
					merchantSreenshotIds : that.merchantSreenshotIds
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showUploadPicModalFlag = false;
				that.loadAppealDetails(that.selectAppeal.id);
			});
		},

		merchantCancelAppeal : function(appealId) {
			var that = this;
			that.$http.get('/appeal/merchantCancelAppeal', {
				params : {
					appealId : appealId
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadAppealDetails(that.selectAppeal.id);
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
		}
	}
});