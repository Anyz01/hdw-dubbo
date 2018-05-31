/**
 * WebUploader工具类
 */

/**
 * 文件上传
 * url 接收端
 * basePath Uploader.swf根目录
 * id 文件按钮ID
 * thelist 上传文件DIV
 * ctlBtn 上传按钮ID
*/

function WebUploaderInit(url,basePath,id,thelist,ctlBtn){
	var $list = $('#'+thelist),
    $btn = $('#'+ctlBtn);

	var uploader = WebUploader.create({
    	// swf文件路径
    	swf: basePath+'/static/hplus/js/plugins/webuploader/Uploader.swf',

   		// 文件接收服务端。
    	server: url,

    	// 选择文件的按钮。可选。
    	// 内部根据当前运行是创建，可能是input元素，也可能是flash.
    	pick: '#'+id,

    	// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    	resize: true
	});

	// 当有文件被添加进队列的时候
	uploader.on( 'fileQueued', function( file ) {
	    $list.append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">等待上传...</p>' +
	    '</div>' );
	});

	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress .progress-bar');

	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<div class="progress progress-striped active">' +
	          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	          '</div>' +
	        '</div>').appendTo( $li ).find('.progress-bar');
	    }

	    $li.find('p.state').text('上传中');

	    $percent.css( 'width', percentage * 100 + '%' );
	});

	//文件成功、失败处理
	//文件上传失败会派送uploadError事件，成功则派送uploadSuccess事件。不管成功或者失败，在文件上传完后都会触发uploadComplete事件。
	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).find('p.state').text('已上传');
	});

	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('上传出错');
	});

	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').fadeOut();
	});

	$btn.on( 'click', function() {
       uploader.upload();
    });

}

/**
 * 图片上传
 * url 接收端
 * basePath Uploader.swf根目录
 * id 文件按钮ID
*/
function WebUploaderImgInit(url,basePath,id){
	// 初始化Web Uploader
	var uploader = WebUploader.create({

	    // 选完文件后，是否自动上传。
	    auto: true,

	    // swf文件路径
	    swf: basePath+'/static/hplus/js/plugins/webuploader/Uploader.swf',

	    // 文件接收服务端。
	    server: url,

	    // 选择文件的按钮。可选。
        pick: {id:'#'+id,  //选择文件的按钮
                multiple:true},   //允许可以同时选择多个图片
     	// 图片质量，只有type为`image/jpeg`的时候才有效。
        quality: 100,

	    threads:'5',        //同时运行5个线程传输
        fileNumLimit:'10',  //文件总数量只能选择10个 

	    // 只允许选择图片文件。
	    accept: {
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/*'
	    }
	});

	// 当有文件添加进来的时候
	uploader.on( 'fileQueued', function( file ) {
	    var $li = $(
	            '<div id="' + file.id + '" class="file-item thumbnail">' +
	                '<img>' +
	                '<div class="info">' + file.name + '</div>' +
	            '</div>'
	            ),
	        $img = $li.find('img');


	    // $list为容器jQuery实例
	    $list.append( $li );

	    // 创建缩略图
	    // 如果为非图片文件，可以不用调用此方法。
	    // thumbnailWidth x thumbnailHeight 为 100 x 100
	    uploader.makeThumb( file, function( error, src ) {
	        if ( error ) {
	            $img.replaceWith('<span>不能预览</span>');
	            return;
	        }

	        $img.attr( 'src', src );
	    }, thumbnailWidth, thumbnailHeight );
	});


	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress span');

	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<p class="progress"><span></span></p>')
	                .appendTo( $li )
	                .find('span');
	    }

	    $percent.css( 'width', percentage * 100 + '%' );
	});

	// 文件上传成功，给item添加成功class, 用样式标记上传成功。
	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).addClass('upload-state-done');
	});

	// 文件上传失败，显示上传出错。
	uploader.on( 'uploadError', function( file ) {
	    var $li = $( '#'+file.id ),
	        $error = $li.find('div.error');

	    // 避免重复创建
	    if ( !$error.length ) {
	        $error = $('<div class="error"></div>').appendTo( $li );
	    }

	    $error.text('上传失败');
	});

	// 完成上传完了，成功或者失败，先删除进度条。
	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').remove();
	});

}