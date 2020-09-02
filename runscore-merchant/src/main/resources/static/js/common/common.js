Vue.http.interceptors.push(function(request) {
	return function(response) {
		if (response.body.code == 999) {
			layer.open({
				title : '提示',
				icon : '7',
				closeBtn : 0,
				btn : [],
				content : '系统检测到你已离线,请重新登录!',
				time : 2000,
				end : function() {
					window.location.href = '/login';
				}
			});
		} else if (response.body.code != 200) {
			response.ok = false;
			layer.alert(response.body.msg, {
				title : '提示',
				icon : 7,
				time : 3000
			});
		}
	};
});
var GLOBAL = {
	systemSetting : {
		currencyUnit : ''
	}
};
initGlobal();

function initGlobal() {
	var systemSetting = sessionStorage.getItem('systemSetting');
	if (systemSetting != null) {
		GLOBAL.systemSetting = JSON.parse(systemSetting);
	} else {
		loadSystemSetting();
	}
}

function loadSystemSetting() {
	var that = this;
	Vue.http.get('/masterControl/getSystemSetting').then(function(res) {
		GLOBAL.systemSetting = res.body.data;
		sessionStorage.setItem('systemSetting', JSON.stringify(GLOBAL.systemSetting));
	});
}

/**
 * 由于js存在精度丢失的问题,需要对其进行四舍五入处理
 * 
 * @param num
 * @param digit
 *            小数位数, 不填则默认4为小数
 * @returns
 */
function numberFormat(num, digit) {
	if (digit == null) {
		digit = 4;
	}
	return parseFloat(Number(num).toFixed(digit));
}

/**
 * 获取url参数
 * 
 * @param name
 * @returns
 */
function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
