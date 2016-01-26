var cvAnimationUtils = {
	
	startRotateAnimationForElement: function (element) {
		// wrapping animated item
		$_wrapper = $('<div>').addClass('block-loader-wrapper');
		$_animatedItem = $(element).wrap($_wrapper);
		// creating spinner block
		$_loaderBlock = cvAnimationUtils.createRotateAnimationLoaderBlock();
		// inserting spinner after animated item
		$_animatedItem.after($_loaderBlock);
	},

	createRotateAnimationLoaderBlock: function () {
		$_loaderBlock = $('<div>').addClass('block-loader');
		$_insideBlockLoader = $('<div>').addClass('loader fa fa-refresh')
			.appendTo($_loaderBlock);
		return $_loaderBlock;
	},
	
	stopRotateAnimationForElement: function (element) {
		$_animatedItem = $(element);
		$_animatedItem.parent().find('.block-loader').remove();
		$_animatedItem.unwrap();
	}
		
};

/* start spinner animation for education list which should last for 3s  */
$(function() {
	var $_educationListItem = $('.education-list');
	
	cvAnimationUtils.startRotateAnimationForElement($_educationListItem);
	
	setTimeout(function() {
		cvAnimationUtils.stopRotateAnimationForElement($_educationListItem);
	}, 3000);
});