function log () {
	for ( let i = 0; i < arguments.length; i++ ) {
		console.log(arguments[i])
	}
}

function relSelector ( obj1, obj2 ) {
	let obj_1 = document.querySelector(obj1);
	let obj_2 = obj_1.querySelector(obj2);
	return obj_2;
}

function relSelectorAll ( obj1, obj2 ) {
	return obj1.querySelectorAll( obj2 );
}

function selector (obj) {
	return document.querySelector( obj )
}

function createElem (elem) {
	return document.createElement( elem )
}

function createText (text) {
	return document.createTextNode( text )
}

