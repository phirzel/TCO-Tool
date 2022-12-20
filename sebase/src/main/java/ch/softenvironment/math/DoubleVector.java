package ch.softenvironment.math;

/**
 * This is a simple C-Array Vector used for vectorial (mathematical sense) operations. Ths vector is not ment to be resized after initialization.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 */
public class DoubleVector {

	protected double[] v;        // type of Vector elements is double

	/**
	 * Construct a DoubleVector from a given array
	 */
	public DoubleVector(double[] array) {
		this(array.length);

		if (size() >= 0) System.arraycopy(array, 0, v, 0, size());
	}

	/**
	 * Create a Vector of size initialCapacity and default elements = 0.
	 *
	 * @param initialCapacity int
	 */
	public DoubleVector(int initialCapacity) {
		super();
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
		v = Double.valueOf[initialCapacity];
		for (int i = 0; i < initialCapacity; i++) {
			// set default initial value
			v[i] = 0;
		}
	}

	/**
	 * return a constant vector of given scalar
	 */
	public DoubleVector(DoubleVector a) {
		this(a.size());

		if (a.size() >= 0) System.arraycopy(a.v, 0, v, 0, a.size());
	}

	/**
	 * Vector Division by Scalar (v1 + v2)
	 */
	public DoubleVector divide(double scalar) {
		for (int i = 0; i < size(); i++) {
			v[i] = v[i] / scalar;
		}
		return (this);
	}

	/**
	 * dot (inner product)
	 */
	public double dot(DoubleVector a) {
		if (size() != a.size()) {
			// throw range  exit(OUTOFRANGE);
		}

		double r = 0;
		for (int i = 0; i < size(); i++) {
			r += v[i] * a.v[i];
		}
		return (r);
	}

	/**
	 * @param obj the reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the obj
	 *     argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		DoubleVector vec = (DoubleVector) obj;

		if (size() != vec.size()) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			if (v[i] != vec.v[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return element at Index in Vector
	 */
	public double get(int index) {
		rangeCheck(index);
		return v[index];
	}

	/**
	 * Length of a Vector (german: Betrag) |vec| = sqrt(vec*vec)
	 */
	public double getAbs() {
		double sum = 0;
		for (int i = 0; i < size(); i++) {
			sum += v[i] * v[i];
		}
		return (Math.sqrt(sum));
	}

	/**
	 * Set all values of Vector to constant scalar value
	 */
	public static DoubleVector getConstantInstance(int vectorSize, double scalar) {
        DoubleVector vec = Double.valueOfVector(vectorSize);

		for (int i = 0; i < vectorSize; i++) {
			vec.v[i] = scalar;
		}

		return vec;
	}

	/**
	 * cross (outer product) where c (return value) is orthogonal to a and b,
	 *
	 * @return DoubleVector (|c| = parallelogramm-area built by a and b)
	 */
	public DoubleVector getCross(DoubleVector b) {
		if ((size() < 3) || (b.size() < 3)) {
			// out of 3D makes no sense
			// throw range()        exit(OUTOFRANGE);
		}

        DoubleVector c = Double.valueOfVector(size());
		c.v[0] = v[1] * b.v[2] - v[2] * b.v[1];
		c.v[1] = -v[0] * b.v[2] + v[2] * b.v[0];
		c.v[2] = v[0] * b.v[1] - v[1] * b.v[0];
		return c;
	}

	/**
	 * Vector Multiplication by a Matrix (r = m * v)
	 */
	public DoubleVector getMultiply(DoubleMatrix m) {
		if (m.getNumberOfVectors() != size()) {
			//throw range(OUTOFRANGE);
		}

        DoubleVector r = Double.valueOfVector(m.getVectorSize());
		for (int i = 0; i < m.getVectorSize(); i++) {
			// rows of M r[i]=a[i]*b
			r.v[i] = 0;
			for (int j = 0; j < m.getNumberOfVectors(); j++) {
				// cols of row of M (internally m[i * m.nbrOfVectors() + j])
				r.v[i] += m.getCell(i, j) * v[j];
			}
		}
		return (r);
	}

	/**
	 * return length of a vector (Norm)
	 */
	public DoubleVector getNorm() {
        double dist = getAbs();
        DoubleVector n = Double.valueOfVector(size());

		if (dist == 0.0) {
			//throw( zero-length vectors cannot be normalized)
		}

		for (int i = 0; i < size(); i++) {
			n.v[i] = v[i] / dist;
		}
		return n; // unit vector
	}

	/**
	 * spat product: volume of prisma built by ((a cross b) dot c)
	 */
	public double getSpat(DoubleVector b, DoubleVector c) {
		return getCross(b).dot(c);
	}

	/**
	 * Vector Subtraction (v1 - v2) where (this -= a)
	 */
	public DoubleVector minus(DoubleVector a) {
		sizeCheck(a);

		for (int i = 0; i < size(); i++) {
			v[i] = v[i] - a.v[i];
		}

		return this;
	}

	/**
	 * Vector Multiplication (v * scalar)
	 */
	public DoubleVector multiply(double scalar) {
		for (int i = 0; i < size(); i++) {
			v[i] = v[i] * scalar;
		}
		return this;
	}

	/**
	 * Vector Addition (v1 + v2) where (this += a)
	 */
	public DoubleVector plus(DoubleVector a) {
		sizeCheck(a);

		for (int i = 0; i < v.length; i++) {
			v[i] = v[i] + a.v[i];
		}

		return this;
	}

	/**
	 * Check if the given index is in range.  If not, throw an appropriate runtime exception.
	 */
	private void rangeCheck(int index) {
		if (index >= size() || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
	}

	/**
	 * Replaces the element at the specified position in this list with the specified element.
	 *
	 * @param index index of element to replace.
	 * @param element element to be stored at the specified position.
	 * @return the element previously at the specified position.
	 * @throws IndexOutOfBoundsException if index out of range
	 *     <tt>(index &lt; 0 || index &gt;= size())</tt>.
	 */
	public void set(int index, double element) {
		rangeCheck(index);

		v[index] = element;
	}

	/**
	 * Return the size of the Vector
	 */
	public int size() {
		return v.length;
	}

	/**
	 * Check if the given index is in range.  If not, throw an appropriate runtime exception.
	 */
	private void sizeCheck(DoubleVector a) {
		if (size() != a.size()) {
			throw new IndexOutOfBoundsException("Vectors not of same size: " + size() + " !=  " + a.size());
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
		for (int i = 0; i < size(); i++) {
			if (i != 0) {
				str += ", ";
			}
			str += v[i];
		}
		return str + "]";
	}
}
