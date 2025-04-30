package parser.prat_parser.model;

public enum BindingPower {
	// Prefix operators
	PREFIX_PLUS_MINUS(9),

	// Postfix operators
	POSTFIX_EXCL_BRACKET(11),

	// Infix operators
	INFIX_ASSIGN(2, 1), INFIX_TERNARY(4, 3), INFIX_ADD_SUB(5, 6), INFIX_MUL_DIV(7, 8), INFIX_DOT(14, 13);

	private final int left;
	private final int right;

	// Prefix/Postfix
	BindingPower(int bp) {
		this.left = bp;
		this.right = bp;
	}

	// Infix
	BindingPower(int left, int right) {
		this.left = left;
		this.right = right;
	}

	public int left() {
		return left;
	}

	public int right() {
		return right;
	}
}
