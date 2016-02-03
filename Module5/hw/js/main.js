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
	var $_educationList = $('.education-list');
	
	cvAnimationUtils.startRotateAnimationForElement($_educationList);
	
	setTimeout(function() {
		cvAnimationUtils.stopRotateAnimationForElement($_educationList);
	}, 3000);
});


/* init portfolio filtering using isotope plugin */
$(function() {
	var $_portfolioList = $('.portfolio-list');
    var $_portfolioNavigationItems = $('.portfolio-nav li');

    $_portfolioNavigationItems.on('click', function() {
        $_portfolioNavigationItems.removeClass('active');
        $(this).addClass('active');

        $_portfolioList.isotope({
            itemSelector: 'li'
        });

        var filterCategory = $(this).data('filter');

        $_portfolioList.isotope({
            filter: filterCategory,
            animationOptions: {
                duration: 1000,
                easing: 'easeOutQuart',
                queue: false
            }
        });

	});
});