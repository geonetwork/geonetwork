/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.legacy;

import lombok.EqualsAndHashCode;

/**
 * Just a container of 2 elements. Good for returning 2 values.
 *
 * @author jesse
 */
@EqualsAndHashCode
public class Pair<R, L> {
    private R one;
    private L two;

    protected Pair() {}

    private Pair(R one, L two) {
        super();
        this.one = one;
        this.two = two;
    }

    public static <R, L> Pair<R, L> read(R one, L two) {
        return new Pair<R, L>(one, two);
    }

    public static <R, L> Pair<R, L> write(R one, L two) {
        return new Writeable<R, L>(one, two);
    }

    public R one() {
        return one;
    }

    public L two() {
        return two;
    }

    @Override
    public String toString() {
        return "[" + one + "," + two + "]";
    }

    public static class Writeable<R, L> extends Pair<R, L> {
        public Writeable(R one, L two) {
            super(one, two);
        }

        public Writeable<R, L> one(R newVal) {
            super.one = newVal;
            return this;
        }

        public Writeable<R, L> two(L newVal) {
            super.two = newVal;
            return this;
        }
    }
}
