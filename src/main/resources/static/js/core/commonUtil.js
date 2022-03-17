var commonUtil = {
	getRankText : function(prv, curr) {
		var result = "";
		if(prv == 0 && curr == 0) return 0;
		if(prv == curr) {
			return curr + "(" + prv + ")";
		}
		if(prv == 0 && curr != 0) {
			return curr;
		}
		if(prv < curr) {
			return curr + '(↓' + prv + ')';
		}
		if(prv > curr) {
			return curr + '(↑' + prv + ')';
		}
		return result;
	},
	ifNull: function(txt) {
		if(txt == null || txt == '' || txt == undefined) return true;
		else return false;
	}
}