function log () {
	for ( let i = 0; i < arguments.length; i++ ) {
		console.log(arguments[i])
	}
}

const relSelector = ( obj1, obj2 ) => {
	return obj1.querySelector( obj2 )
}

const relSelectorAll = ( obj1, obj2 ) => {
	return obj1.querySelectorAll( obj2 );
}

const selector = (obj) => {
	return document.querySelector( obj )
}

const createElem = (elem) => {
	return document.createElement( elem )
}

const createText = (text) => {
	return document.createTextNode( text )
}