var systemNoticeVM = new Vue({
	el : '#systemNotice',
	data : {
		global : GLOBAL,
		systemNotices : []
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '系统公告';
		headerVM.showBackFlag = true;
		this.loadNotice();
	},
	methods : {

		loadNotice : function() {
			var that = this;
			that.$http.get('/systemNotice/findTop10PublishedNotice').then(function(res) {
				this.systemNotices = res.body.data;
			});
		},

	}
});