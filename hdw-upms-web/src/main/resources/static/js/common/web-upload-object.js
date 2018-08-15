/**
 * web-upload 工具类
 * 
 * @author TuMinglong
 */
(function () {
    /**
     * 文件上传
     * @param {*} url 上传服务器地址
     * @param {*} id 选择文件按钮ID picker
     * @param {*} list 文件预览DIV的ID thelist
     * @param {*} btn 开始上传按钮ID ctlBtn
     */
	var FileWebUpload = function (url, id, list, btn) {
		this.url = url;
		this.id = id;
		this.list;
		this.btn = btn;
	};

	FileWebUpload.prototype = {
        /**
        * 初始化webUploader
        */
		init: function () {
			var $ = jQuery,
			    $list = $('#' + this.list),
				$btn = $('#' + this.btn),
				state = 'pending',
				uploader;

			uploader = WebUploader.create({

				// 不压缩image
				resize: false,

				// swf文件路径
				swf: hdw.ctxPath + '/static/hplus/js/plugins/webuploader/Uploader.swf',

				// 文件接收服务端。
				server: this.url,

				// 选择文件的按钮。可选。
				// 内部根据当前运行是创建，可能是input元素，也可能是flash.
				pick: '#' + this.id
			});

			// 当有文件添加进来的时候
			uploader.on('fileQueued', function (file) {
				$list.append('<div id="' + file.id + '" class="item">' +
					'<h4 class="info">' + file.name + '</h4>' +
					'<p class="state">等待上传...</p>' +
					'</div>');
			});

			// 文件上传过程中创建进度条实时显示。
			uploader.on('uploadProgress', function (file, percentage) {
				var $li = $('#' + file.id),
					$percent = $li.find('.progress .progress-bar');

				// 避免重复创建
				if (!$percent.length) {
					$percent = $('<div class="progress progress-striped active">' +
						'<div class="progress-bar" role="progressbar" style="width: 0%">' +
						'</div>' +
						'</div>').appendTo($li).find('.progress-bar');
				}

				$li.find('p.state').text('上传中');

				$percent.css('width', percentage * 100 + '%');
			});

			uploader.on('uploadSuccess', function (file) {
				$('#' + file.id).find('p.state').text('已上传');
			});

			uploader.on('uploadError', function (file) {
				$('#' + file.id).find('p.state').text('上传出错');
			});

			uploader.on('uploadComplete', function (file) {
				$('#' + file.id).find('.progress').fadeOut();
			});

			uploader.on('all', function (type) {
				if (type === 'startUpload') {
					state = 'uploading';
				} else if (type === 'stopUpload') {
					state = 'paused';
				} else if (type === 'uploadFinished') {
					state = 'done';
				}

				if (state === 'uploading') {
					$btn.text('暂停上传');
				} else {
					$btn.text('开始上传');
				}
			});
		},
	};

	window.FileWebUpload = FileWebUpload;

	/**
	 * 图片上传
	 * @param {*} url 上传服务器路径
	 * @param {*} id  选择图片按钮 filePicker
	 * @param {*} fileList 文件预览DIV的ID fileList
	 */
	var ImageWebUpload = function (url, id, fileList) {
		this.url = url;
		this.id = id;
		this.fileList = fileList;
	};

	ImageWebUpload.prototype = {
		/**
	   	 * 初始化webUploader
	   	 */
		init: function () {
			var $ = jQuery,
				$list = $('#'+ this.fileList) ,
				// 优化retina, 在retina下这个值是2
				ratio = window.devicePixelRatio || 1,

				// 缩略图大小
				thumbnailWidth = 100 * ratio,
				thumbnailHeight = 100 * ratio,

				// Web Uploader实例
				uploader;

			// 初始化Web Uploader
			uploader = WebUploader.create({

				// 自动上传。
				auto: true,

				// swf文件路径
				swf: zx.ctxPath + '/static/hplus/js/plugins/webuploader/Uploader.swf',

				// 文件接收服务端。
				server: this.url,

				// 选择文件的按钮。可选。
				// 内部根据当前运行是创建，可能是input元素，也可能是flash.
				pick: '#' + this.id,

				// 只允许选择文件，可选。
				accept: {
					title: 'Images',
					extensions: 'gif,jpg,jpeg,bmp,png',
					mimeTypes: 'image/*'
				}
			});

			// 当有文件添加进来的时候
			uploader.on('fileQueued', function (file) {
				var $li = $(
					'<div id="' + file.id + '" class="file-item thumbnail">' +
					'<img>' +
					'<div class="info">' + file.name + '</div>' +
					'</div>'
				),
					$img = $li.find('img');

				$list.append($li);

				// 创建缩略图
				uploader.makeThumb(file, function (error, src) {
					if (error) {
						$img.replaceWith('<span>不能预览</span>');
						return;
					}

					$img.attr('src', src);
				}, thumbnailWidth, thumbnailHeight);
			});

			// 文件上传过程中创建进度条实时显示。
			uploader.on('uploadProgress', function (file, percentage) {
				var $li = $('#' + file.id),
					$percent = $li.find('.progress span');

				// 避免重复创建
				if (!$percent.length) {
					$percent = $('<p class="progress"><span></span></p>')
						.appendTo($li)
						.find('span');
				}

				$percent.css('width', percentage * 100 + '%');
			});

			// 文件上传成功，给item添加成功class, 用样式标记上传成功。
			uploader.on('uploadSuccess', function (file) {
				$('#' + file.id).addClass('upload-state-done');
			});

			// 文件上传失败，现实上传出错。
			uploader.on('uploadError', function (file) {
				var $li = $('#' + file.id),
					$error = $li.find('div.error');

				// 避免重复创建
				if (!$error.length) {
					$error = $('<div class="error"></div>').appendTo($li);
				}

				$error.text('上传失败');
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on('uploadComplete', function (file) {
				$('#' + file.id).find('.progress').remove();
			});
		},
	};
	window.ImageWebUpload = ImageWebUpload;

}());