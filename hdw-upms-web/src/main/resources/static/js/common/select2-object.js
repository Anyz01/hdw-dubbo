/**
 * select2插件的封装
 */
(function() {

	var $Select2List = function(id, url) {
		this.id = id;
		this.url = url;
		this.minimumInputLength = 1;// 可选1个
	};

	$Select2List.prototype = {
		/**
		 * 初始化Select2List
		 */
		init : function() {
			var control = $('#' + this.id);
			$.ajax({
				url : this.url,
				type : 'post',
				dataType : 'json',
				cache : true,
				success : function(data) {
					control.empty();
					$.each(data, function(i, item) {
						if (item.selected) {
							control.append("<option value='" + item.id
									+ "' selected='selected'>"
									+ item.text + "</option>");
						}else{
                            if(i==0){
                                control.append("<option value=''></option>");
                            }
							control.append("<option value='" + item.id + "'>" + item.text + "</option>");
						}
					});
				}
			});
			// 设置Select2的处理
			control.select2({
				allowClear : true,
				placeholder : "请选择",
				language : "zh-CN",
				escapeMarkup : function(m) {
					return m;
				}
			});
		}
	};

	window.$Select2List = $Select2List;

}());

