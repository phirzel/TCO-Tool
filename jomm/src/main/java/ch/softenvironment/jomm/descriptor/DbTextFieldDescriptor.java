package ch.softenvironment.jomm.descriptor;
/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * Describes any String fields.
 *
 * @author Peter Hirzel
 * @since 1.3 (2006-07-05)
 */
public class DbTextFieldDescriptor extends DbFieldTypeDescriptor {

    /**
     * Length of a maximal Text or Memo.
     */
    public static final long MEMO = Long.MAX_VALUE;
    /**
     * Length of a DbNlsString.
     */
    public static final long NLS = 255;

    private transient long length = 0;

    /**
     * DbTextFieldDescriptor constructor comment.
     */
    public DbTextFieldDescriptor(long length) {
        super(String.class);

        if (length <= 0) {
            throw new ch.softenvironment.util.DeveloperException("value must be > 0");
        }
        this.length = length;
    }

    /**
     * Return the length of the Text-Field.
     */
    public long getLength() {
        return length;
    }
}
