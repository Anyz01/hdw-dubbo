/**
 * select2插件的封装
 */
(function () {
    var Select2List = function (id, url) {
        this.id = id;
        this.url = url;
        this.minimumInputLength = 1;// 可选1个
        this.arrayObj = [];
        this.placeholder = '请选择';
    };

    Select2List.prototype = {
        /**
         * 初始化Select2List
         */
        init: function () {
            var temp=this.arrayObj;
            var control = $('#' + this.id);
            $.ajax({
                url: this.url,
                type: 'get',
                dataType: 'json',
                cache: true,
                success: function (data) {
                    control.empty();
                    $.each(data, function (i, item) {
                        if (temp != null && temp.length > 0) {
                            if (contains(temp, item.id)) {
                                control.append("<option value='" + item.id + "' selected='selected'>&nbsp;" + item.text + "</option>");
                            } else {
                                control.append("<option value='" + item.id + "'>&nbsp;" + item.text + "</option>");
                            }
                        } else {
                            if (i == 0) {
                                control.append("<option value=''></option>");
                            }
                            control.append("<option value='" + item.id + "'>&nbsp;" + item.text + "</option>");
                        }
                    });
                }
            });
            //设置Select2的处理
            control.select2({
                allowClear: true,
                placeholder: this.placeholder,
                language: "zh-CN",
                escapeMarkup: function (m) {
                    return m;
                }
            });
            //判断数组中是否存在某值
            function contains(arr, obj) {
                var i = arr.length;
                while (i--) {
                    if (arr[i] === obj) {
                        return true;
                    }
                }
                return false;
            }
        },
        /**
         * 设置最小选择长度
         */
        setMinimumInputLength: function (minimumInputLength) {
            this.minimumInputLength = minimumInputLength;
        },
        /**
         * 设置选择文字
         */
        setPlaceholder: function (placeholder) {
            this.placeholder = placeholder;
        },
        /**
         * 设置选中数组
         */
        setArrayObj: function (arrayObj) {
            this.arrayObj = arrayObj;
        },
    };

    window.Select2List = Select2List;

}());

