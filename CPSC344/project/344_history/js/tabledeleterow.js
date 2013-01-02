// tabledeleterow.js version 1.2 2006-02-21
// mredkj.com
//http://www.mredkj.com/tutorials/tabledeleterow.html

// CONFIG notes. Below are some comments that point to where this script can be customized.
// Note: Make sure to include a <tbody></tbody> in your table's HTML

var INPUT_NAME_PREFIX = 'inputName'; // this is being set via script
var RADIO_NAME = 'totallyrad'; // this is being set via script
var TABLE_NAME = 'tblSample'; // this should be named in the HTML
var ROW_BASE = 38592853; // first number (for display)
var hasLoaded = false;

window.onload=fillInRows;

function fillInRows()
{
	hasLoaded = true;
}

// CONFIG:
// myRowObject is an object for storing information about the table rows
function myRowObject(one, two, three, four)
{
	this.one = one; // text object - id
	this.two = two; // text object - name
	this.three = three; // text object - permission
	this.four = four; // button - delete
}

/*
 * addRowToTable
 * Inserts at row 'num', or appends to the end if no arguments are passed in. Don't pass in empty strings.
 */
function addRowToTable(num)
{
	if (hasLoaded) {
		var tbl = document.getElementById(TABLE_NAME);
		var nextRow = tbl.tBodies[0].rows.length;
		var iteration = nextRow + ROW_BASE;
		if (num == null) { 
			num = nextRow;
		} else {
			iteration = num + ROW_BASE;
		}
		
		// add the row
		var row = tbl.tBodies[0].insertRow(num);
		
		// CONFIG: requires classes named classy0 and classy1
		row.className = 'classy' + (iteration % 2);
	
		// CONFIG: This whole section can be configured
		
		// cell 0 - text - user id
		var cell0 = row.insertCell(0);
		var textNode = document.createTextNode(iteration);
		cell0.appendChild(textNode);
		
		// cell 1 -  text - user name
		var cell1 = row.insertCell(1);
		var txtInp = document.createElement('input');
		txtInp.setAttribute('type', 'text');
		txtInp.setAttribute('name', INPUT_NAME_PREFIX + iteration);
		txtInp.setAttribute('size', '40');
		txtInp.setAttribute('value', iteration); // iteration included for debug purposes
		cell1.appendChild(txtInp);
		
		// cell 3 - permission type
		var cell2 = row.insertCell(2);
		var cbEl = document.createElement('input');
		var cbEl = document.createElement('input');
		cbEl.setAttribute('type', 'text');
		cbEl.setAttribute('name', INPUT_NAME_PREFIX + iteration);
		cbEl.setAttribute('size', '40');
		cbEl.setAttribute('value', iteration); // iteration included for debug purposes
		cbEl.appendChild(cbEl);
		
		// cell 2 - input button
		var cell3 = row.insertCell(3);
		var btnEl = document.createElement('input');
		btnEl.setAttribute('type', 'button');
		btnEl.setAttribute('value', 'Delete');
		btnEl.onclick = function () {deleteCurrentRow(this)};
		cell2.appendChild(btnEl);
		

		
		/*// cell 3 - input checkbox
		var cell3 = row.insertCell(3);
		var cbEl = document.createElement('input');
		cbEl.setAttribute('type', 'checkbox');
		cell3.appendChild(cbEl);
		
		// cell 4 - input radio
		var cell4 = row.insertCell(4);
		var raEl;
		try {
			raEl = document.createElement('<input type="radio" name="' + RADIO_NAME + '" value="' + iteration + '">');
			var failIfNotIE = raEl.name.length;
		} catch(ex) {
			raEl = document.createElement('input');
			raEl.setAttribute('type', 'radio');
			raEl.setAttribute('name', RADIO_NAME);
			raEl.setAttribute('value', iteration);
		}
		cell4.appendChild(raEl);
		*/
		
		// Pass in the elements you want to reference later
		// Store the myRow object in each row
		//row.myRow = new myRowObject(textNode, txtInp, cbEl, raEl);
		row.myRow = new myRowObject(textNode, txtInp, cbEl, btnEl);
	}
}

// If there isn't an element with an onclick event in your row, then this function can't be used.
function deleteCurrentRow(obj)
{
	if (hasLoaded) {
		var delRow = obj.parentNode.parentNode;
		var tbl = delRow.parentNode.parentNode;
		var rIndex = delRow.sectionRowIndex;
		var rowArray = new Array(delRow);
		deleteRows(rowArray);
		reorderRows(tbl, rIndex);
	}
}

function deleteRows(rowObjArray)
{
	if (hasLoaded) {
		for (var i=0; i<rowObjArray.length; i++) {
			var rIndex = rowObjArray[i].sectionRowIndex;
			rowObjArray[i].parentNode.deleteRow(rIndex);
		}
	}
}
