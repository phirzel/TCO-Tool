package ch.softenvironment.math;

/**
 * Mathematical Matrix with rows and columns.
 *
 * @author Peter Hirzel
 */
public class DoubleMatrix {

	protected double[] m; // 1 dimensional representation with type double
	private int vectorSize;
	private int nbrOfVectors;

	/**
	 * This is a dummy constructor => try to get rid of it
	 */
	public DoubleMatrix() {
		super();
	}

	/**
	 * Create a quadratic Matrix.
	 */
	public DoubleMatrix(int size) {
		this(size, size);
	}

	/**
	 * Create a Matrix and set elements initially to 0.
	 */
	public DoubleMatrix(int vectorSize, int nbrOfVectors) {
		if ((vectorSize < 0) || (nbrOfVectors < 0)) {
			throw new IndexOutOfBoundsException("Index (Dimensions too small)");
		}

		this.vectorSize = vectorSize;
		this.nbrOfVectors = nbrOfVectors;
		m = new double[vectorSize * nbrOfVectors];
		for (int i = 0; i < m.length; i++) {
			// set default initial value
			m[i] = 0;
		}
	}

	/**
	 * DoubleMatrix constructor comment.
	 */
	public DoubleMatrix(DoubleMatrix a) {
		this(a.getVectorSize(), a.getNumberOfVectors());
		System.arraycopy(a.m, 0, m, 0, m.length);
	}

	/**
	 * @param obj the reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the obj
	 *     argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		DoubleMatrix mat = (DoubleMatrix) obj;

		if ((obj == null) || (getVectorSize() != mat.getVectorSize()) || (getNumberOfVectors() != mat.getNumberOfVectors())) {
			return false;
		}
		for (int i = 0; i < m.length; i++) {
			if (m[i] != mat.m[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return this multiplicated instance Falk scheme c=m*a
	 */
	public DoubleMatrix falk(DoubleMatrix a) {
		DoubleMatrix c = getFalk(a);
		m = c.m;

		return this;
	}

	/**
	 * Creates an adjacency Matrix
	 */
	public static DoubleMatrix getAdjacencyInstance(DoubleMatrix a) {
		DoubleMatrix adj = new DoubleMatrix();
		//TODO NYI: following algorithm
/*
/*
procedure adj(matr:mat;var res:mat);
(* Berechnet die Adjunkte einer Matrix *)
  var     i,k:integer;
	  min:mat;
	  sign:integer;
  begin
	if matr.x=matr.y then begin (*dimension n x n*)
	  res.x:=matr.x;
	  res.y:=matr.y;
	  for i:=1 to matr.x do         (*column*)
	for k:=1 to matr.y do begin (*row*)
	  minor(matr,i,k,min);
	  if (i+k) mod 2=0 then sign:=1
			   else sign:=-1;
	  res.field[i,k]:=sign*det(min);
	end;
	  trn(res,res);
	  end (*if*)
	else begin (*error*)
	  err:=true;
	end; (*else*)
end; (*adj*)
*/

		return adj;
	}

	/**
	 * Returns the matrix element m(i,j)
	 */
	public double getCell(int row, int col) {
		if ((row < 0) || (vectorSize <= row) || (col < 0) || (nbrOfVectors <= col)) {
			throw new IndexOutOfBoundsException("getCell()");
		}
		return (m[row * nbrOfVectors + col]);
	}

	/**
	 * Set all values of Matrix to constant scalar value
	 */
	public static DoubleMatrix getConstantInstance(int vectorSize, int nbrOfVectors, double scalar) {
		DoubleMatrix con = new DoubleMatrix(vectorSize, nbrOfVectors);

		for (int i = 0; i < vectorSize; i++) {
			for (int j = 0; j < nbrOfVectors; j++) {
				con.setCell(i, j, scalar);
			}
		}

		return con;
	}

	/**
	 * Returns the determinant of this Matrix
	 */
	public double getDeterminant() {
		double det = 0;
		//TODO NYI: following algorithm

/*
function det(matr:mat):real;
(* Berechnet Determinante *)
  var    i:integer;
	 d:real;
	 min:mat;
	 sign:integer;
  begin
	if matr.x=matr.y then begin
	  if matr.x=1 then
	d:=matr.field[1,1]
	  else if matr.x=2 then
	d:=matr.field[1,1]*matr.field[2,2]-matr.field[2,1]*matr.field[1,2]
	  else begin
	d:=0;
	for i:=1 to matr.x do begin
	  if (i+1) mod 2=0 then sign:=1
			   else sign:=-1;
	  minor(matr,1,i,min);
	  d:=d+sign*matr.field[1,i]*det(min);
	end; (*for*)
	  end; (*else*)
	  det:=d;
	  end (*if*)
	else begin (*error*)
	  err:=true;
	  det:=1;
	end; (*else*)
end; (*det*)
*/
		return det;
	}

	/**
	 * Return new instance (Multiplication Result) Falk scheme c=m*a
	 */
	public DoubleMatrix getFalk(DoubleMatrix a) {
		if (getNumberOfVectors() != a.getVectorSize()) {
			throw new IndexOutOfBoundsException("Index (dimension fault)");
		}
		DoubleMatrix c = getConstantInstance(vectorSize, a.nbrOfVectors, 0);
		for (int i = 0; i < vectorSize; i++) {                // rows of M
			for (int j = 0; j < nbrOfVectors; j++) {        // cols of row of M
				for (int k = 0; k < a.nbrOfVectors; k++) {    // cols of A
					c.m[i * c.nbrOfVectors + k] +=
						m[i * nbrOfVectors + j] * a.m[j * a.nbrOfVectors + k];
				}
			}
		}

		return c;
	}

	/**
	 * Create identity matrix where diagonal values are 1, others are 0
	 */
	public static DoubleMatrix getIdentityInstance(int vectorSize) {
		DoubleMatrix idn = getConstantInstance(vectorSize, vectorSize, 0);

		for (int i = 0; i < vectorSize; i++) {
			idn.setCell(i, i, 1);
		}

		return idn;
	}

	/**
	 * Creates an inversed Matrix A(3,4) => AT(4,3)
	 */
	public static DoubleMatrix getInverseInstance(DoubleMatrix a) {
		DoubleMatrix inv = new DoubleMatrix();
		//TODO NYI: following algorithm
/*
procedure inv(matr:mat;var res:mat);
(* Berechnet die Inverse Matrix *)
  var     d         :real; (*scalar: 1/det(matr)*)
	  i,k       :integer;
	  determin  :real;
  begin
	if matr.x=matr.y then begin (*dimension n x n*)
	  res.x:=matr.x;
	  res.y:=matr.y;
	  determin := det(matr);
	  if determin = 0 then err := true
	  else begin
	d:=1/det(matr);
	adj(matr,res);
	for i:=1 to matr.x do   (*column*)
	  for k:=1 to matr.y do (*row*)
	    res.field[i,k]:=d*res.field[i,k];
	end (*else*)
	  end (*if*)
	else begin (*error*)
	  err:=true;
	end; (*else*)
end; (*inv*)
*/

		return inv;
	}

	/**
	 * Creates a minor Matrix
	 */
	public static DoubleMatrix getMinorInstance(DoubleMatrix a, int row, int col) {
		DoubleMatrix minor = new DoubleMatrix();
		//TODO NYI: following algorithm
/*
procedure minor(matr:mat;bx,by:integer;var res:mat);
  var     i,k:integer;
	if (matr.x>1) and (matr.y>1) then begin
	  res.x:=matr.x-1;
	  res.y:=matr.y-1;
	  for i:=1 to matr.x do    (*column*)
	for k:=1 to matr.y do  (*row*)
	  if (i<bx) and (k<by) then
	    res.field[i,k]:=matr.field[i,k]
	  else if (i<bx) and (k>by) then
	    res.field[i,k-1]:=matr.field[i,k]
	  else if (i>bx) and (k<by) then
	    res.field[i-1,k]:=matr.field[i,k]
	  else if (i>bx) and (k>by) then
	    res.field[i-1,k-1]:=matr.field[i,k];
	  end (*if*)
	else begin (*error*)
	  err:=true;
	end; (*else*)
*/

		return minor;
	}

	/**
	 *
	 */
	public int getNumberOfVectors() {
		return nbrOfVectors;
	}

	/**
	 * Power of a quadratic Matrix A^n = AAA...
	 */
	public DoubleMatrix getPower(int exponent) {
		quadraticCheck();

		switch (exponent) {
			case -1:
				return getInverseInstance(this);
			case 0:
				return getIdentityInstance(getVectorSize());
			case 1:
				return this;
			default:
				DoubleMatrix pow = new DoubleMatrix(getVectorSize(), getNumberOfVectors());
				DoubleMatrix h = new DoubleMatrix(getVectorSize(), getNumberOfVectors());
				if (exponent < 0) {
					h = getInverseInstance(this);
				} else {
					// exponent>1
					h = this;
				}
				pow = h;
				for (; exponent > 1; exponent--) {
					pow = pow.getFalk(h);
				}
				return pow;
		}
	}

	/**
	 * Creates a transponed Matrix german: transponierte Matrix A(3,4) => AT(4,3)
	 */
	public DoubleMatrix getTransponed() {
		DoubleMatrix trn = new DoubleMatrix(getNumberOfVectors(), getVectorSize());
		for (int i = 0; i < getVectorSize(); i++) {
			for (int j = 0; j < getNumberOfVectors(); j++) {
				trn.setCell(j, i, getCell(i, j));
			}
		}
		return trn;
	}

	/**
	 *
	 */
	public int getVectorSize() {
		return vectorSize;
	}

	/**
	 * Matrix Subtraction (m - a)
	 */
	public DoubleMatrix minus(DoubleMatrix a) {
		sizeCheck(a);

		for (int i = 0; i < vectorSize * nbrOfVectors; i++) {
			m[i] = m[i] - a.m[i];
		}

		return this;
	}

	/**
	 * Matrix Multiplication by a Scalar (m * scalar)
	 */
	public DoubleMatrix multiply(double scalar) {
		for (int i = 0; i < vectorSize * nbrOfVectors; i++) {
			m[i] = m[i] * scalar;
		}
		return this;
	}

	/**
	 * Matrix Addition (m + a)
	 */
	public DoubleMatrix plus(DoubleMatrix a) {
		sizeCheck(a);

		for (int i = 0; i < vectorSize * nbrOfVectors /*m.length*/; i++) {
			m[i] = m[i] + a.m[i];
		}

		return this;
	}

	/**
	 * Return wether this Matrix is quadratic or not
	 */
	private void quadraticCheck() {
		if (getVectorSize() != getNumberOfVectors()) {
			throw new IndexOutOfBoundsException("Index (non-quadratic): rows = " + getVectorSize() + ", columns = " + getNumberOfVectors());
		}
	}

	/**
	 * Set the matrix element m(i,j)
	 */
	public void setCell(int row, int col, double value) {
		if (row < 0 || vectorSize <= row || col < 0 || nbrOfVectors <= col) {
			throw new IndexOutOfBoundsException("setRange()");
		}
		m[row * nbrOfVectors + col] = value;
	}

	/**
	 * Return wether this Matrix is equal to in dimensions
	 */
	private void sizeCheck(DoubleMatrix a) {
		if ((vectorSize != a.getVectorSize()) && (nbrOfVectors != a.getNumberOfVectors())) {
			throw new IndexOutOfBoundsException("Index (matrices of non-equal size)");
		}
	}

	/**
	 * Returns a string representation of the vector.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < getVectorSize(); i++) {
			if (i > 0) {
				str += " ";
			}
			str += "[";
			for (int j = 0; j < getNumberOfVectors(); j++) {
				if (j != 0) {
					str += ", ";
				}
				str += getCell(i, j);
			}
			str += "]";
			if (!(i == getNumberOfVectors() - 1)) {
				str += "\n";
			}
		}
		return str + "]";
	}
}
