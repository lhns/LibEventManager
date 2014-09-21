package com.dafttech.storage.tuple;

public class GenericTuple {
    public static class _1<T0> extends ArrayTuple {
        /**
         *
         */
        private static final long serialVersionUID = -5260647872308179286L;

        public final T0 _0;

        public _1(T0 _0) {
            super(_0);
            this._0 = _0;
        }

        protected _1(T0 _0, Object _1) {
            super(_0, _1);
            this._0 = _0;
        }

        protected _1(T0 _0, Object _1, Object _2) {
            super(_0, _1, _2);
            this._0 = _0;
        }

        protected _1(T0 _0, Object _1, Object _2, Object _3) {
            super(_0, _1, _2, _3);
            this._0 = _0;
        }
    }

    public static class _2<T0, T1> extends _1<T0> {
        /**
         *
         */
        private static final long serialVersionUID = 7232404047664929618L;

        public final T1 _1;

        public _2(T0 _0, T1 _1) {
            super(_0, _1);
            this._1 = _1;
        }

        protected _2(T0 _0, T1 _1, Object _2) {
            super(_0, _1, _2);
            this._1 = _1;
        }

        protected _2(T0 _0, T1 _1, Object _2, Object _3) {
            super(_0, _1, _2, _3);
            this._1 = _1;
        }
    }

    public static class _3<T0, T1, T2> extends _2<T0, T1> {
        /**
         *
         */
        private static final long serialVersionUID = 4778020259519958789L;

        public final T2 _2;

        public _3(T0 _0, T1 _1, T2 _2) {
            super(_0, _1, _2);
            this._2 = _2;
        }

        protected _3(T0 _0, T1 _1, T2 _2, Object _3) {
            super(_0, _1, _2, _3);
            this._2 = _2;
        }
    }

    public static class _4<T0, T1, T2, T3> extends _3<T0, T1, T2> {
        /**
         *
         */
        private static final long serialVersionUID = 864480216854675185L;

        public final T3 _3;

        public _4(T0 _0, T1 _1, T2 _2, T3 _3) {
            super(_0, _1, _2, _3);
            this._3 = _3;
        }
    }
}
