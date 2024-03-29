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
 * Target-Field with Date-Character.
 *
 * @author Peter Hirzel
 * @since 1.1.1.1 (2005-04-25)
 */
public class DbDateFieldDescriptor extends DbFieldTypeDescriptor {

    public static final int DATE = 0;
    public static final int TIME = 1;
    public static final int DATETIME = 2;

    private int type = 0;

    /**
     * DbFieldDescriptor constructor comment.
     */
    public DbDateFieldDescriptor(final int type) {
        super(java.util.Date.class);

        this.type = type;
    }

    /**
     * Kind of Date.
     */
    public int getType() {
        return type;
    }
}
