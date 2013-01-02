/**
 * ########################################################
 * jVertTabs - JQuery plugin for creating vertical tabs.
 * By Seth Lenzi - slenzi@gmail.com
 * This is free! Do with it as you want!
 * MIT License. - http://en.wikipedia.org/wiki/MIT_License
 * ########################################################
 *
 * Change History:
 *
 * June 30, 2010 - v1.1.4 ---- CSS Updates. IE-8 fixes relating to height, width, & margin settings.
 *
 * March 3, 2010 - v1.1.3 ---- Added code to ensure that all vtabs-content-panel elements are at least as tall as the tabColumn element.
 *							   This is to address issue #1, http://code.google.com/p/jquery-vert-tabs/issues/detail?id=1
 * 
 * February 8, 2010 - v1.1.2 - More bug fixes...sigh. Plugin now keeps track of options for each tab set via the 
 *                             allTabOptions object.
 *
 * February 6, 2010 - v1.1.1 - Bug fix dealing with varying length tabs that go beyond the default CSS width of 150px.
 *						     
 * February 5, 2010 - v1.1 --- Added set selected tab function.
 *						       (e.g. $("#elm").jVertTabs('selected',2); // select 3rd tab, 0-based.)
 *
 * February 3, 2010 - v1.0 --- Initial release.
 *
 *
 * ------------------------------------------------------
 *
 * default options
 *
 * selected: Index of the tab to open on initialization. 0-based, first tab is 0, second tab is 1, etc..
 *
 * select: A callback function that is called when a tab is clicked. The 'index' value will be the index of
 *         the tab that was clicked. 0-based, first tab is 0, second tab is 1, etc..
 *
 * spinner: Text to show during ajax request. Pass in empty string to deactivate that behavior.
 *
 * equalHeights: If set to true the content panels will all have the same height. The min-heigh value
 *               for each panel will be set to that of the tallest panel in the group. By default this
 *               feature is turned off, or "false". Set to "true" to turn on.
 */ 
(function($) {

	// keep track of options for each tab group
	var allTabOptions = new Array();

	/**
	 * String startsWith function. 
	 */
	String.prototype.startsWith = function(str){
		return (this.match("^"+str)==str);
	}
	
	$.fn.jVertTabs = function(attr,options) {
	
		var elmId = $(this).attr('id');
	
		var opts;
		var defaults = {
			selected: 0,
			select: function(index){
				//alert("Tab " + index + " clicked.");
			},
			spinner: "Retrieving data...",
			equalHeights: false
		};

		var tabColumnHeight = 0;
	
		// Check to see if an object is a plain object (created using "{}" or "new Object").
		if($.isPlainObject(attr)){
			
			// This block will be executed when we call our plugin with options:
			// $("#elmId").jVertTabsDev({
			// 		selected: 1
			// });
			
			// extend default options with any that were provided by user
			options = attr;
			opts = $.extend(defaults, options);
			allTabOptions[elmId] = opts;
			
		}else{
			
			// This block will be executed when we call our plugin like so:
			// $("#elmId").jVertTabsDev();
			// Or..
			// $("#elmId").jVertTabsDev('active',true);
		
			if(attr != null && options != null){
				if(attr == "selected"){
					//alert("a attr: " + attr + ", options: " + options);
					var thisTabOpts = allTabOptions[elmId];
					//alert(elmId + " before: " + thisTabOpts.selected);
					thisTabOpts.selected = options;
					//alert(elmId + " after: " + thisTabOpts.selected);
					/* add css classes to elements */
					var tabRoot = $(this);
					doSelectTab($(this),options);
					return;
				}
			}else{
			
				//alert("b attr: " + attr + ", options: " + options);
			
				// extend default options with any that were provided by user
				opts = $.extend(defaults, options);
				allTabOptions[elmId] = opts;			
			
			}
		}
		
		// apply jVertTabs to all matching elements
        return this.each(function() {
		
			/* add css classes to elements */
			var tabRoot = $(this);
			setStyle(tabRoot);
		
			/* references to tab column and tab content column */
			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabContentColumn = tabRoot.children("div.vtabs-content-column");
			//tabColumnHeight = tabColumn.height();
			
			/* locate all li elements  */
			$(this).find(".vtabs-tab-column > ul > li").each(function(i){
				
				/* set css for initial state of tabs. first tab is open, the rest are closed.*/
				if(i < 1){
					$(this).addClass("open");
					$(this).find("a").addClass("open");
				}else{
					$(this).addClass("closed");
					$(this).find("a").addClass("closed");
				}
				
				/* add click events to all li elements */
				$(this).click(function() {
					handleTabClick($(this),i,tabRoot,true);
					return false;
				});
			});
			
			/* set initial state of tab content panels. first panel is open, the rest are closed */
			$(this).children(".vtabs-content-column > div.vtabs-content-panel").each(function(i){
				if(i>0){
					$(this).hide();
				}		
			});
			
			/* open specified tab on itialization. this is customizable via the 'selected' option */
			var thisTabOpts = allTabOptions[elmId];
			if(thisTabOpts != null){
				var preSelectLi = tabColumn.find("ul > li").eq(thisTabOpts.selected);
				handleTabClick(preSelectLi,thisTabOpts.selected,tabRoot,false);
			}

			/* equalize heights if user specified to do so */
			var thisTabOpts = allTabOptions[elmId];
			if(thisTabOpts != null && thisTabOpts.equalHeights){
				equalizeHeights(tabContentColumn);
			}

			/* make sure that the content panels are not shorter than the tabColumn element */
			tabColumnHeight = getTotalTabsHeight(tabRoot);
			setMinHeight(tabContentColumn,tabColumnHeight);			
			
			adjustMargin(tabRoot);
			
        });
		
		/**
		 * Selects a tab (opens the tab.)
		 *
		 * tabRoot - Reference to the root tab element.
		 * index - the tab to open, 0-based index. 0 = first tab, 1 = second tab, etc...
		 */
		function doSelectTab(tabRoot,index){
			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabContentColumn = tabRoot.children("div.vtabs-content-column");
			var selectLi = tabColumn.find("ul > li").eq(index);			
			handleTabClick(selectLi,index,tabRoot,true);
		}
		
		/**
		 * Click event handler.
		 *
		 * li 			      - <li></li> element that was clicked
		 * liIndex		 	  - index of the <li></li> element that was clicked. 0-based.
		 * tabRoot 			  - Reference to the root tab element.
		 * doSelectedCallBack - true to call the 'select' callback function, false not to.
		 */
		function handleTabClick(li,liIndex,tabRoot,doSelectedCallBack){

			var elmId = tabRoot.attr('id');
			var tabCol = tabRoot.children("div.vtabs-tab-column");
			var tabContentCol = tabRoot.children("div.vtabs-content-column");
					
			/* set css to closed for ones that are currently open */
			tabCol.find("ul > li").each(function(i){
				if($(this).hasClass("open")){
					$(this).removeClass("open").addClass("closed");
					$(this).find("a").removeClass("open").addClass("closed");
				}
			});
			/* set css for tab that was clicked */ 
			li.removeClass("closed").addClass("open");
			li.find("a").removeClass("closed").addClass("open");		
				
			/* hide all content panels and get reference to panel that needs to be showed. */
			var openContentPanel;
			tabContentCol.children("div.vtabs-content-panel").each(function(i){
				$(this).hide();
				if(i == liIndex){
					openContentPanel = $(this);
				}
			});
			
			/* get link ahref value to see if we need to make an ajax call */
			var link = li.find("a");
			var linkText = link.text();
			var linkValue = link.attr("href");
			if(!linkValue.startsWith("#")){
				// set spinner message on link if we have a spinner value
				if(opts.spinner != ""){
					link.text(opts.spinner);
				}
				// make ajax call to get data
				$.ajax({
					url: linkValue,
					type: "POST",
					//dataType: "html",
					success: function(data) {
						// set data
						openContentPanel.html(data);				
						// open panel
						openContentPanel.fadeIn(1000);				
						// set link text back to what it originally was
						link.text(linkText);
						/* re-equalize heights if user specified to do so */
						var thisTabOpts = allTabOptions[elmId];
						if(thisTabOpts != null && thisTabOpts.equalHeights){
							equalizeHeights(tabContentCol);
						}					
					},
					error: function(request,status,errorThrown) {
						// set link text back to what it originally was
						link.text(linkText);
						// alert error to user
						alert("Error requesting " + linkValue + ": " + errorThrown);
					}
				});
			}else{
				// no ajax request, open the panel
				openContentPanel.fadeIn(1000);
			}
			
			/* see if the user provided an optional callback function to call when a tab is clicked */
			var thisTabOpts = allTabOptions[elmId];
			if(thisTabOpts != null && doSelectedCallBack){
				if(jQuery.isFunction(thisTabOpts.select)){
					thisTabOpts.select.call(this,liIndex);
				}
			}		
			
		};
		
		/**
		 * Loop through all tabs and sum all their heights.
		 */
		function getTotalTabsHeight(tabRoot){
			/* locate all li elements  */
			var height = 0;
			tabRoot.find(".vtabs-tab-column > ul > li").each(function(i){
				//height += parseInt( $(this).css("height").replace("px","") );
				height += $(this).outerHeight(true);
			});
			return height;
		}		
		
		/**
		 * Sets the height (min-height) of all content panels to that of the tallest one.
		 *
		 * tabContentCol - reference to the #vtabs-content-column element
		 */
		function equalizeHeights(tabContentCol){
			var tallest = getTallestHeight(tabContentCol);
			//alert("Equalize heights to: " + tallest);
			setMinHeight(tabContentCol,tallest);
		};
		
		/**
		 * Iterates through all content panels and gets the height of the tallest one.
		 *
		 * tabContentCol - reference to the #vtabs-content-column element
		 */
		function getTallestHeight(tabContentCol){
			var maxHeight = 0, currentHeight = 0;
			tabContentCol.children("div.vtabs-content-panel").each(function(i){
				//currentHeight = parseInt( $(this).css("height").replace("px","") );
				currentHeight = $(this).height();
				if(currentHeight > maxHeight){
					maxHeight = currentHeight;
				}
			});
			return maxHeight;
		};
		
		/**
		 * Iterates through all content panels and sets the min-height value for each one.
		 *
		 * tabContentCol - reference to the #vtabs-content-column element
		 * minHeight - the min-height value
		 */
		function setMinHeight(tabContentCol,minHeight){
			var panelHeight = 0;
			tabContentCol.children("div.vtabs-content-panel").each(function(i){
				panelHeight = $(this).height();
				if(panelHeight < minHeight){
					$(this).css("min-height",minHeight);
					//$(this).css("height",minHeight);
					// set height if IE
					if ($.browser.msie) {
						//$(this).css("height",minHeight);
					}
				}
			});
		};
		
		/**
		 * Adds the tab css classes to all the elements.
		 *
		 * tabRoot - reference to the root tab element.
		 */
		function setStyle(tabRoot){
			tabRoot.addClass("vtabs");
			tabRoot.children("div").eq(0).addClass("vtabs-tab-column");
			tabRoot.children("div").eq(1).addClass("vtabs-content-column");
			tabRoot.children("div").eq(1).children("div").addClass("vtabs-content-panel");
		};

		/**
		 * Adjusts the left margin of the content column so it lines up with the edges of the tabs.
		 *
		 * tabRoot - reference to the root tab element.
		 */		
		function adjustMargin(tabRoot){			
			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabColWidth = tabColumn.width();
			$(tabRoot).children('div.vtabs-content-column').css({"margin-left": tabColWidth-1 + "px"});
			//alert("Client Width of tabColumn: " + $(tabColumn).get(0).clientWidth);	 // convert jquery object to DOM element using get(0) call.	
		}

	};
		
})(jQuery);