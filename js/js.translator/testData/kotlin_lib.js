/**
 * Copyright 2010 Tim Down.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
    "use strict";

    // Shims for String
    String.prototype.startsWith = function (s) {
        return this.indexOf(s) === 0;
    };

    String.prototype.endsWith = function (s) {
        return this.indexOf(s, this.length - s.length) !== -1;
    };

    String.prototype.contains = function (s) {
        return this.indexOf(s) !== -1;
    };

    // Kotlin stdlib

    Kotlin.equals = function (obj1, obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }

        if (Array.isArray(obj1)) {
            return Kotlin.arrayEquals(obj1, obj2);
        }

        if (typeof obj1 == "object" && obj1.equals_za3rmp$ !== undefined) {
            return obj1.equals_za3rmp$(obj2);
        }

        if(obj1 instanceof Kotlin.Long && obj2 instanceof Kotlin.Long) {
            return obj1.equals(obj2);
        }

        return obj1 === obj2;
    };

    Kotlin.hashCode = function (obj) {
        if (obj == null) {
            return 0;
        }
        if ("function" == typeof obj.hashCode) {
            return obj.hashCode();
        }
        var objType = typeof obj;
        if ("object" == objType || "function" == objType) {
            return getObjectHashCode(obj);
        } else if ("number" == objType) {
            // TODO: a more elaborate code is needed for floating point values.
            return obj | 0;
        } if ("boolean" == objType) {
            return Number(obj)
        }

        var str = String(obj);
        return getStringHashCode(str);
    };

    Kotlin.toString = function (o) {
        if (o == null) {
            return "null";
        }
        else if (Array.isArray(o)) {
            return Kotlin.arrayToString(o);
        }
        else {
            return o.toString();
        }
    };

    Kotlin.arrayToString = function (a) {
        return "[" + a.join(", ") + "]";
    };

    Kotlin.compareTo = function(a, b) {
        return a < b ? -1 : a > b ? 1 : 0;
    };

    Kotlin.toShort = function(a) {
        return (a & 0xFFFF) << 16 >> 16;
    };

    Kotlin.toByte = function(a) {
        return (a & 0xFF) << 24 >> 24;
    };

    Kotlin.intUpto = function (from, to) {
        return new Kotlin.NumberRange(from, to);
    };

    Kotlin.intDownto = function (from, to) {
        return new Kotlin.Progression(from, to, -1);
    };

    Kotlin.RuntimeException = Kotlin.createClassNow();
    Kotlin.NullPointerException = Kotlin.createClassNow();
    Kotlin.NoSuchElementException = Kotlin.createClassNow();
    Kotlin.IllegalArgumentException = Kotlin.createClassNow();
    Kotlin.IllegalStateException = Kotlin.createClassNow();
    Kotlin.UnsupportedOperationException = Kotlin.createClassNow();
    Kotlin.IOException = Kotlin.createClassNow();

    Kotlin.throwNPE = function () {
        throw new Kotlin.NullPointerException();
    };

    function throwAbstractFunctionInvocationError(funName) {
        return function () {
            var message;
            if (funName !== undefined) {
                message = "Function " + funName + " is abstract";
            }
            else {
                message = "Function is abstract";
            }
            throw new TypeError(message);
        };
    }

    /** @const */
    var POW_2_32 = 4294967296;
    // TODO: consider switching to Symbol type once we are on ES6.
    /** @const */
    var OBJECT_HASH_CODE_PROPERTY_NAME = "kotlinHashCodeValue$";

    function getObjectHashCode(obj) {
        if (!(OBJECT_HASH_CODE_PROPERTY_NAME in obj)) {
            var hash = (Math.random() * POW_2_32) | 0; // Make 32-bit singed integer.
            Object.defineProperty(obj, OBJECT_HASH_CODE_PROPERTY_NAME, { value:  hash, enumerable: false });
        }
        return obj[OBJECT_HASH_CODE_PROPERTY_NAME];
    }

    function getStringHashCode(str) {
        var hash = 0;
        for (var i = 0; i < str.length; i++) {
            var code  = str.charCodeAt(i);
            hash  = (hash * 31 + code) | 0; // Keep it 32-bit.
        }
        return hash;
    }

    /**
     * @interface
     * @template T
     */
    Kotlin.Iterator = Kotlin.createClassNow(null, null, {
        next: throwAbstractFunctionInvocationError("Iterator#next"),
        hasNext: throwAbstractFunctionInvocationError("Iterator#hasNext")
    });

    /**
     * @class
     * @implements {Kotlin.Iterator.<T>}
     *
     * @constructor
     * @param {Array.<T>} array
     * @template T
     */
    var ArrayIterator = Kotlin.createClassNow(Kotlin.Iterator,
        /** @constructs */
        function (array) {
            this.array = array;
            this.index = 0;
        },
        /** @lends {ArrayIterator.prototype} */
        {
            next: function () {
                return this.array[this.index++];
            },
            hasNext: function () {
                return this.index < this.array.length;
            },
            remove: function () {
                if (this.index < 0 || this.index > this.array.length) throw new RangeError();
                this.index--;
                this.array.splice(this.index, 1);
            }
    });

    /**
     * @class
     * @extends {ArrayIterator.<T>}
     *
     * @constructor
     * @param {Kotlin.AbstractList.<T>} list
     * @template T
     */
    var ListIterator = Kotlin.createClassNow(ArrayIterator,
        /** @constructs */
        function (list) {
            this.list = list;
            this.size = list.size();
            this.index = 0;
        }, {
            next: function () {
                return this.list.get(this.index++);
            }
    });

    /**
     * @interface
     * @template T
     */
    Kotlin.Collection = Kotlin.createClassNow();

    Kotlin.Enum = Kotlin.createClassNow(null,
        function () {
            this.name$ = undefined;
            this.ordinal$ = undefined;
        }, {
            name: function () {
                return this.name$;
            },
            ordinal: function () {
                return this.ordinal$;
            },
            toString: function () {
                return this.name();
            }
        }
    );

    Kotlin.PropertyMetadata = Kotlin.createClassNow(null,
        function (name) {
            this.name = name;
        }
    );

    Kotlin.AbstractCollection = Kotlin.createClassNow(Kotlin.Collection, null, {
        addAll_xeylzf$: function (collection) {
            var modified = false;
            var it = collection.iterator();
            while (it.hasNext()) {
                if (this.add_za3rmp$(it.next())) {
                    modified = true;
                }
            }
            return modified
        },
        removeAll_xeylzf$: function (c) {
            var modified = false;
            var it = this.iterator();
            while (it.hasNext()) {
                if (c.contains_za3rmp$(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified
        },
        retainAll_xeylzf$: function (c) {
            var modified = false;
            var it = this.iterator();
            while (it.hasNext()) {
                if (!c.contains_za3rmp$(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified
        },
        containsAll_xeylzf$: function (c) {
            var it = c.iterator();
            while (it.hasNext()) {
                if (!this.contains_za3rmp$(it.next())) return false;
            }
            return true;
        },
        isEmpty: function () {
            return this.size() === 0;
        },
        iterator: function () {
            return new ArrayIterator(this.toArray());
        },
        equals_za3rmp$: function (o) {
            if (this.size() !== o.size()) return false;

            var iterator1 = this.iterator();
            var iterator2 = o.iterator();
            var i = this.size();
            while (i-- > 0) {
                if (!Kotlin.equals(iterator1.next(), iterator2.next())) {
                    return false;
                }
            }

            return true;
        },
        toString: function () {
            var builder = "[";
            var iterator = this.iterator();
            var first = true;
            var i = this.size();
            while (i-- > 0) {
                if (first) {
                    first = false;
                }
                else {
                    builder += ", ";
                }
                builder += iterator.next();
            }
            builder += "]";
            return builder;
        },
        toJSON: function () {
            return this.toArray();
        }
    });

    /**
     * @interface // actually it's abstract class
     * @template T
     */
    Kotlin.AbstractList = Kotlin.createClassNow(Kotlin.AbstractCollection, null, {
        iterator: function () {
            return new ListIterator(this);
        },
        remove_za3rmp$: function (o) {
            var index = this.indexOf_za3rmp$(o);
            if (index !== -1) {
                this.remove_za3lpa$(index);
                return true;
            }
            return false;
        },
        contains_za3rmp$: function (o) {
            return this.indexOf_za3rmp$(o) !== -1;
        }
    });

    //TODO: should be JS Array-like (https://developer.mozilla.org/en-US/docs/JavaScript/Guide/Predefined_Core_Objects#Working_with_Array-like_objects)
    Kotlin.ArrayList = Kotlin.createClassNow(Kotlin.AbstractList,
        function () {
            this.array = [];
        }, {
            get_za3lpa$: function (index) {
                this.checkRange(index);
                return this.array[index];
            },
            set_vux3hl$: function (index, value) {
                this.checkRange(index);
                this.array[index] = value;
            },
            size: function () {
                return this.array.length;
            },
            iterator: function () {
                return Kotlin.arrayIterator(this.array);
            },
            add_za3rmp$: function (element) {
                this.array.push(element);
                return true;
            },
            add_vux3hl$: function (index, element) {
                this.array.splice(index, 0, element);
            },
            addAll_xeylzf$: function (collection) {
                var it = collection.iterator();
                for (var i = this.array.length, n = collection.size(); n-- > 0;) {
                    this.array[i++] = it.next();
                }
            },
            remove_za3lpa$: function (index) {
                this.checkRange(index);
                return this.array.splice(index, 1)[0];
            },
            clear: function () {
                this.array.length = 0;
            },
            indexOf_za3rmp$: function (o) {
                for (var i = 0; i < this.array.length; i++) {
                    if (Kotlin.equals(this.array[i], o)) {
                        return i;
                    }
                }
                return -1;
            },
            lastIndexOf_za3rmp$: function (o) {
                for (var i = this.array.length - 1; i >= 0; i--) {
                    if (Kotlin.equals(this.array[i], o)) {
                        return i;
                    }
                }
                return -1;
            },
            toArray: function () {
                return this.array.slice(0);
            },
            toString: function () {
                return "[" + this.array.join(", ") + "]";
            },
            toJSON: function () {
                return this.array;
            },
            checkRange: function (index) {
                if (index < 0 || index >= this.array.length) {
                    throw new RangeError();
                }
            }
        });

    Kotlin.Runnable = Kotlin.createClassNow(null, null, {
        run: throwAbstractFunctionInvocationError("Runnable#run")
    });

    Kotlin.Comparable = Kotlin.createClassNow(null, null, {
        compareTo: throwAbstractFunctionInvocationError("Comparable#compareTo")
    });

    Kotlin.Appendable = Kotlin.createClassNow(null, null, {
        append: throwAbstractFunctionInvocationError("Appendable#append")
    });

    Kotlin.Closeable = Kotlin.createClassNow(null, null, {
        close: throwAbstractFunctionInvocationError("Closeable#close")
    });

    Kotlin.safeParseInt = function (str) {
        var r = parseInt(str, 10);
        return isNaN(r) ? null : r;
    };

    Kotlin.safeParseDouble = function (str) {
        var r = parseFloat(str);
        return isNaN(r) ? null : r;
    };

    Kotlin.arrayEquals = function (a, b) {
        if (a === b) {
            return true;
        }
        if (!Array.isArray(b) || a.length !== b.length) {
            return false;
        }

        for (var i = 0, n = a.length; i < n; i++) {
            if (!Kotlin.equals(a[i], b[i])) {
                return false;
            }
        }
        return true;
    };

    Kotlin.System = function () {
        var output = "";

        var print = function (obj) {
            if (obj !== undefined) {
                if (obj === null || typeof obj !== "object") {
                    output += obj;
                }
                else {
                    output += obj.toString();
                }
            }
        };
        var println = function (obj) {
            this.print(obj);
            output += "\n";
        };

        return {
            out: function () {
                return {
                    print: print,
                    println: println
                };
            },
            output: function () {
                return output;
            },
            flush: function () {
                output = "";
            }
        };
    }();

    Kotlin.println = function (s) {
        Kotlin.System.out().println(s);
    };

    Kotlin.print = function (s) {
        Kotlin.System.out().print(s);
    };

    Kotlin.RangeIterator = Kotlin.createClassNow(Kotlin.Iterator,
        function (start, end, increment) {
            this.start = start;
            this.end = end;
            this.increment = increment;
            this.i = start;
        }, {
            next: function () {
                var value = this.i;
                this.i = this.i + this.increment;
                return value;
            },
            hasNext: function () {
                if (this.increment > 0)
                    return this.i <= this.end;
                else
                    return this.i >= this.end;
            }
    });

    Kotlin.NumberRange = Kotlin.createClassNow(null,
        function (start, end) {
            this.start = start;
            this.end = end;
            this.increment = 1;
        }, {
            contains: function (number) {
                return this.start <= number && number <= this.end;
            },
            iterator: function () {
                return new Kotlin.RangeIterator(this.start, this.end, this.increment);
            },
            isEmpty: function () {
                return this.start > this.end;
            },
            equals_za3rmp$: function(other) {
                if (other == null)
                    return false;
                return this.start === other.start && this.end === other.end && this.increment === other.increment;
            }
    });

    Kotlin.NumberProgression = Kotlin.createClassNow(null,
        function (start, end, increment) {
            this.start = start;
            this.end = end;
            this.increment = increment;
        }, {
        iterator: function () {
            return new Kotlin.RangeIterator(this.start, this.end, this.increment);
        },
        isEmpty: function() {
            return this.increment > 0 ? this.start > this.end : this.start < this.end;
        }
    });

    /**
     * @interface
     * @template T
     */
    Kotlin.Comparator = Kotlin.createClassNow(null, null, {
        compare: throwAbstractFunctionInvocationError("Comparator#compare")
    });

    /**
     * @class
     * @implements {Kotlin.Comparator.<T>}
     *
     * @constructor
     * @param {function(T,T): Boolean} comparator
     * @template T
     */
    var ComparatorImpl = Kotlin.createClassNow(Kotlin.Comparator,
        function (comparator) {
            this.compare = comparator;
        }
    );

    /**
     * @param {function(T,T): Boolean} f
     * @returns {Kotlin.Comparator.<T>}
     * @template T
     */
    Kotlin.comparator = function (f) {
        return new ComparatorImpl(f);
    };

    Kotlin.collectionsMax = function (c, comp) {
        if (c.isEmpty()) {
            //TODO: which exception?
            throw new Error();
        }
        var it = c.iterator();
        var max = it.next();
        while (it.hasNext()) {
            var el = it.next();
            if (comp.compare(max, el) < 0) {
                max = el;
            }
        }
        return max;
    };

    Kotlin.collectionsSort = function (mutableList, comparator) {
        var boundComparator = undefined;
        if (comparator !== undefined) {
            boundComparator = comparator.compare.bind(comparator);
        }

        if (mutableList instanceof Array) {
            mutableList.sort(boundComparator);
        }

        //TODO: should be deleted when List will be JS Array-like (https://developer.mozilla.org/en-US/docs/JavaScript/Guide/Predefined_Core_Objects#Working_with_Array-like_objects)
        var array = [];
        var it = mutableList.iterator();
        while (it.hasNext()) {
            array.push(it.next());
        }

        array.sort(boundComparator);

        for (var i = 0, n = array.length; i < n; i++) {
            mutableList.set_vux3hl$(i, array[i]);
        }
    };

    Kotlin.copyToArray = function (collection) {
        var array = [];
        var it = collection.iterator();
        while (it.hasNext()) {
            array.push(it.next());
        }

        return array;
    };


    Kotlin.StringBuilder = Kotlin.createClassNow(null,
        function () {
            this.string = "";
        }, {
        append: function (obj, from, to) {
            if (from == undefined && to == undefined) {
                this.string = this.string + obj.toString();
            } else if (to == undefined) {
                this.string = this.string + obj.toString().substring(from);
            } else {
                this.string = this.string + obj.toString().substring(from, to);
            }

            return this;
        },
        reverse: function () {
            this.string = this.string.split("").reverse().join("");
            return this;
        },
        toString: function () {
            return this.string;
        }
    });

    Kotlin.splitString = function (str, regex, limit) {
        return str.split(new RegExp(regex), limit);
    };

    Kotlin.nullArray = function (size) {
        var res = [];
        var i = size;
        while (i > 0) {
            res[--i] = null;
        }
        return res;
    };

    Kotlin.numberArrayOfSize = function (size) {
        return Kotlin.arrayFromFun(size, function () {
            return 0;
        });
    };

    Kotlin.charArrayOfSize = function (size) {
        return Kotlin.arrayFromFun(size, function () {
            return '\0';
        });
    };

    Kotlin.booleanArrayOfSize = function (size) {
        return Kotlin.arrayFromFun(size, function () {
            return false;
        });
    };

    Kotlin.longArrayOfSize = function (size) {
        return Kotlin.arrayFromFun(size, function () {
            return Kotlin.Long.fromInt(0);
        });
    };

    Kotlin.arrayFromFun = function (size, initFun) {
        var result = new Array(size);
        for (var i = 0; i < size; i++) {
            result[i] = initFun(i);
        }
        return result;
    };

    Kotlin.arrayIndices = function (arr) {
        return new Kotlin.NumberRange(0, arr.length - 1);
    };

    Kotlin.arrayIterator = function (array) {
        return new ArrayIterator(array);
    };

    Kotlin.jsonFromTuples = function (pairArr) {
        var i = pairArr.length;
        var res = {};
        while (i > 0) {
            --i;
            res[pairArr[i][0]] = pairArr[i][1];
        }
        return res;
    };

    Kotlin.jsonAddProperties = function (obj1, obj2) {
        for (var p in obj2) {
            if (obj2.hasOwnProperty(p)) {
                obj1[p] = obj2[p];
            }
        }
        return obj1;
    };

    // Copyright 2009 The Closure Library Authors. All Rights Reserved.
    //
    // Licensed under the Apache License, Version 2.0 (the "License");
    // you may not use this file except in compliance with the License.
    // You may obtain a copy of the License at
    //
    //      http://www.apache.org/licenses/LICENSE-2.0
    //
    // Unless required by applicable law or agreed to in writing, software
    // distributed under the License is distributed on an "AS-IS" BASIS,
    // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    /**
     * Constructs a 64-bit two's-complement integer, given its low and high 32-bit
     * values as *signed* integers.  See the from* functions below for more
     * convenient ways of constructing Longs.
     *
     * The internal representation of a long is the two given signed, 32-bit values.
     * We use 32-bit pieces because these are the size of integers on which
     * Javascript performs bit-operations.  For operations like addition and
     * multiplication, we split each number into 16-bit pieces, which can easily be
     * multiplied within Javascript's floating-point representation without overflow
     * or change in sign.
     *
     * In the algorithms below, we frequently reduce the negative case to the
     * positive case by negating the input(s) and then post-processing the result.
     * Note that we must ALWAYS check specially whether those values are MIN_VALUE
     * (-2^63) because -MIN_VALUE == MIN_VALUE (since 2^63 cannot be represented as
     * a positive number, it overflows back into a negative).  Not handling this
     * case would often result in infinite recursion.
     *
     * @param {number} low  The low (signed) 32 bits of the long.
     * @param {number} high  The high (signed) 32 bits of the long.
     * @constructor
     * @final
     */
    Kotlin.Long = function(low, high) {
      /**
       * @type {number}
       * @private
       */
      this.low_ = low | 0;  // force into 32 signed bits.

      /**
       * @type {number}
       * @private
       */
      this.high_ = high | 0;  // force into 32 signed bits.
    };


    // NOTE: Common constant values ZERO, ONE, NEG_ONE, etc. are defined below the
    // from* methods on which they depend.


    /**
     * A cache of the Long representations of small integer values.
     * @type {!Object}
     * @private
     */
    Kotlin.Long.IntCache_ = {};


    /**
     * Returns a Long representing the given (32-bit) integer value.
     * @param {number} value The 32-bit integer in question.
     * @return {!Kotlin.Long} The corresponding Long value.
     */
    Kotlin.Long.fromInt = function(value) {
      if (-128 <= value && value < 128) {
        var cachedObj = Kotlin.Long.IntCache_[value];
        if (cachedObj) {
          return cachedObj;
        }
      }

      var obj = new Kotlin.Long(value | 0, value < 0 ? -1 : 0);
      if (-128 <= value && value < 128) {
        Kotlin.Long.IntCache_[value] = obj;
      }
      return obj;
    };


    /**
     * Returns a Long representing the given value, provided that it is a finite
     * number.  Otherwise, zero is returned.
     * @param {number} value The number in question.
     * @return {!Kotlin.Long} The corresponding Long value.
     */
    Kotlin.Long.fromNumber = function(value) {
      if (isNaN(value) || !isFinite(value)) {
        return Kotlin.Long.ZERO;
      } else if (value <= -Kotlin.Long.TWO_PWR_63_DBL_) {
        return Kotlin.Long.MIN_VALUE;
      } else if (value + 1 >= Kotlin.Long.TWO_PWR_63_DBL_) {
        return Kotlin.Long.MAX_VALUE;
      } else if (value < 0) {
        return Kotlin.Long.fromNumber(-value).negate();
      } else {
        return new Kotlin.Long(
            (value % Kotlin.Long.TWO_PWR_32_DBL_) | 0,
            (value / Kotlin.Long.TWO_PWR_32_DBL_) | 0);
      }
    };


    /**
     * Returns a Long representing the 64-bit integer that comes by concatenating
     * the given high and low bits.  Each is assumed to use 32 bits.
     * @param {number} lowBits The low 32-bits.
     * @param {number} highBits The high 32-bits.
     * @return {!Kotlin.Long} The corresponding Long value.
     */
    Kotlin.Long.fromBits = function(lowBits, highBits) {
      return new Kotlin.Long(lowBits, highBits);
    };


    /**
     * Returns a Long representation of the given string, written using the given
     * radix.
     * @param {string} str The textual representation of the Long.
     * @param {number=} opt_radix The radix in which the text is written.
     * @return {!Kotlin.Long} The corresponding Long value.
     */
    Kotlin.Long.fromString = function(str, opt_radix) {
      if (str.length == 0) {
        throw Error('number format error: empty string');
      }

      var radix = opt_radix || 10;
      if (radix < 2 || 36 < radix) {
        throw Error('radix out of range: ' + radix);
      }

      if (str.charAt(0) == '-') {
        return Kotlin.Long.fromString(str.substring(1), radix).negate();
      } else if (str.indexOf('-') >= 0) {
        throw Error('number format error: interior "-" character: ' + str);
      }

      // Do several (8) digits each time through the loop, so as to
      // minimize the calls to the very expensive emulated div.
      var radixToPower = Kotlin.Long.fromNumber(Math.pow(radix, 8));

      var result = Kotlin.Long.ZERO;
      for (var i = 0; i < str.length; i += 8) {
        var size = Math.min(8, str.length - i);
        var value = parseInt(str.substring(i, i + size), radix);
        if (size < 8) {
          var power = Kotlin.Long.fromNumber(Math.pow(radix, size));
          result = result.multiply(power).add(Kotlin.Long.fromNumber(value));
        } else {
          result = result.multiply(radixToPower);
          result = result.add(Kotlin.Long.fromNumber(value));
        }
      }
      return result;
    };


    // NOTE: the compiler should inline these constant values below and then remove
    // these variables, so there should be no runtime penalty for these.


    /**
     * Number used repeated below in calculations.  This must appear before the
     * first call to any from* function below.
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_16_DBL_ = 1 << 16;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_24_DBL_ = 1 << 24;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_32_DBL_ =
        Kotlin.Long.TWO_PWR_16_DBL_ * Kotlin.Long.TWO_PWR_16_DBL_;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_31_DBL_ =
        Kotlin.Long.TWO_PWR_32_DBL_ / 2;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_48_DBL_ =
        Kotlin.Long.TWO_PWR_32_DBL_ * Kotlin.Long.TWO_PWR_16_DBL_;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_64_DBL_ =
        Kotlin.Long.TWO_PWR_32_DBL_ * Kotlin.Long.TWO_PWR_32_DBL_;


    /**
     * @type {number}
     * @private
     */
    Kotlin.Long.TWO_PWR_63_DBL_ =
        Kotlin.Long.TWO_PWR_64_DBL_ / 2;


    /** @type {!Kotlin.Long} */
    Kotlin.Long.ZERO = Kotlin.Long.fromInt(0);


    /** @type {!Kotlin.Long} */
    Kotlin.Long.ONE = Kotlin.Long.fromInt(1);


    /** @type {!Kotlin.Long} */
    Kotlin.Long.NEG_ONE = Kotlin.Long.fromInt(-1);


    /** @type {!Kotlin.Long} */
    Kotlin.Long.MAX_VALUE =
        Kotlin.Long.fromBits(0xFFFFFFFF | 0, 0x7FFFFFFF | 0);


    /** @type {!Kotlin.Long} */
    Kotlin.Long.MIN_VALUE = Kotlin.Long.fromBits(0, 0x80000000 | 0);


    /**
     * @type {!Kotlin.Long}
     * @private
     */
    Kotlin.Long.TWO_PWR_24_ = Kotlin.Long.fromInt(1 << 24);


    /** @return {number} The value, assuming it is a 32-bit integer. */
    Kotlin.Long.prototype.toInt = function() {
      return this.low_;
    };


    /** @return {number} The closest floating-point representation to this value. */
    Kotlin.Long.prototype.toNumber = function() {
      return this.high_ * Kotlin.Long.TWO_PWR_32_DBL_ +
             this.getLowBitsUnsigned();
    };


    /**
     * @param {number=} opt_radix The radix in which the text should be written.
     * @return {string} The textual representation of this value.
     * @override
     */
    Kotlin.Long.prototype.toString = function(opt_radix) {
      var radix = opt_radix || 10;
      if (radix < 2 || 36 < radix) {
        throw Error('radix out of range: ' + radix);
      }

      if (this.isZero()) {
        return '0';
      }

      if (this.isNegative()) {
        if (this.equals(Kotlin.Long.MIN_VALUE)) {
          // We need to change the Long value before it can be negated, so we remove
          // the bottom-most digit in this base and then recurse to do the rest.
          var radixLong = Kotlin.Long.fromNumber(radix);
          var div = this.div(radixLong);
          var rem = div.multiply(radixLong).subtract(this);
          return div.toString(radix) + rem.toInt().toString(radix);
        } else {
          return '-' + this.negate().toString(radix);
        }
      }

      // Do several (6) digits each time through the loop, so as to
      // minimize the calls to the very expensive emulated div.
      var radixToPower = Kotlin.Long.fromNumber(Math.pow(radix, 6));

      var rem = this;
      var result = '';
      while (true) {
        var remDiv = rem.div(radixToPower);
        var intval = rem.subtract(remDiv.multiply(radixToPower)).toInt();
        var digits = intval.toString(radix);

        rem = remDiv;
        if (rem.isZero()) {
          return digits + result;
        } else {
          while (digits.length < 6) {
            digits = '0' + digits;
          }
          result = '' + digits + result;
        }
      }
    };


    /** @return {number} The high 32-bits as a signed value. */
    Kotlin.Long.prototype.getHighBits = function() {
      return this.high_;
    };


    /** @return {number} The low 32-bits as a signed value. */
    Kotlin.Long.prototype.getLowBits = function() {
      return this.low_;
    };


    /** @return {number} The low 32-bits as an unsigned value. */
    Kotlin.Long.prototype.getLowBitsUnsigned = function() {
      return (this.low_ >= 0) ?
          this.low_ : Kotlin.Long.TWO_PWR_32_DBL_ + this.low_;
    };


    /**
     * @return {number} Returns the number of bits needed to represent the absolute
     *     value of this Long.
     */
    Kotlin.Long.prototype.getNumBitsAbs = function() {
      if (this.isNegative()) {
        if (this.equals(Kotlin.Long.MIN_VALUE)) {
          return 64;
        } else {
          return this.negate().getNumBitsAbs();
        }
      } else {
        var val = this.high_ != 0 ? this.high_ : this.low_;
        for (var bit = 31; bit > 0; bit--) {
          if ((val & (1 << bit)) != 0) {
            break;
          }
        }
        return this.high_ != 0 ? bit + 33 : bit + 1;
      }
    };


    /** @return {boolean} Whether this value is zero. */
    Kotlin.Long.prototype.isZero = function() {
      return this.high_ == 0 && this.low_ == 0;
    };


    /** @return {boolean} Whether this value is negative. */
    Kotlin.Long.prototype.isNegative = function() {
      return this.high_ < 0;
    };


    /** @return {boolean} Whether this value is odd. */
    Kotlin.Long.prototype.isOdd = function() {
      return (this.low_ & 1) == 1;
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long equals the other.
     */
    Kotlin.Long.prototype.equals = function(other) {
      return (this.high_ == other.high_) && (this.low_ == other.low_);
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long does not equal the other.
     */
    Kotlin.Long.prototype.notEquals = function(other) {
      return (this.high_ != other.high_) || (this.low_ != other.low_);
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long is less than the other.
     */
    Kotlin.Long.prototype.lessThan = function(other) {
      return this.compare(other) < 0;
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long is less than or equal to the other.
     */
    Kotlin.Long.prototype.lessThanOrEqual = function(other) {
      return this.compare(other) <= 0;
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long is greater than the other.
     */
    Kotlin.Long.prototype.greaterThan = function(other) {
      return this.compare(other) > 0;
    };


    /**
     * @param {Kotlin.Long} other Long to compare against.
     * @return {boolean} Whether this Long is greater than or equal to the other.
     */
    Kotlin.Long.prototype.greaterThanOrEqual = function(other) {
      return this.compare(other) >= 0;
    };


    /**
     * Compares this Long with the given one.
     * @param {Kotlin.Long} other Long to compare against.
     * @return {number} 0 if they are the same, 1 if the this is greater, and -1
     *     if the given one is greater.
     */
    Kotlin.Long.prototype.compare = function(other) {
      if (this.equals(other)) {
        return 0;
      }

      var thisNeg = this.isNegative();
      var otherNeg = other.isNegative();
      if (thisNeg && !otherNeg) {
        return -1;
      }
      if (!thisNeg && otherNeg) {
        return 1;
      }

      // at this point, the signs are the same, so subtraction will not overflow
      if (this.subtract(other).isNegative()) {
        return -1;
      } else {
        return 1;
      }
    };


    /** @return {!Kotlin.Long} The negation of this value. */
    Kotlin.Long.prototype.negate = function() {
      if (this.equals(Kotlin.Long.MIN_VALUE)) {
        return Kotlin.Long.MIN_VALUE;
      } else {
        return this.not().add(Kotlin.Long.ONE);
      }
    };


    /**
     * Returns the sum of this and the given Long.
     * @param {Kotlin.Long} other Long to add to this one.
     * @return {!Kotlin.Long} The sum of this and the given Long.
     */
    Kotlin.Long.prototype.add = function(other) {
      // Divide each number into 4 chunks of 16 bits, and then sum the chunks.

      var a48 = this.high_ >>> 16;
      var a32 = this.high_ & 0xFFFF;
      var a16 = this.low_ >>> 16;
      var a00 = this.low_ & 0xFFFF;

      var b48 = other.high_ >>> 16;
      var b32 = other.high_ & 0xFFFF;
      var b16 = other.low_ >>> 16;
      var b00 = other.low_ & 0xFFFF;

      var c48 = 0, c32 = 0, c16 = 0, c00 = 0;
      c00 += a00 + b00;
      c16 += c00 >>> 16;
      c00 &= 0xFFFF;
      c16 += a16 + b16;
      c32 += c16 >>> 16;
      c16 &= 0xFFFF;
      c32 += a32 + b32;
      c48 += c32 >>> 16;
      c32 &= 0xFFFF;
      c48 += a48 + b48;
      c48 &= 0xFFFF;
      return Kotlin.Long.fromBits((c16 << 16) | c00, (c48 << 16) | c32);
    };


    /**
     * Returns the difference of this and the given Long.
     * @param {Kotlin.Long} other Long to subtract from this.
     * @return {!Kotlin.Long} The difference of this and the given Long.
     */
    Kotlin.Long.prototype.subtract = function(other) {
      return this.add(other.negate());
    };


    /**
     * Returns the product of this and the given long.
     * @param {Kotlin.Long} other Long to multiply with this.
     * @return {!Kotlin.Long} The product of this and the other.
     */
    Kotlin.Long.prototype.multiply = function(other) {
      if (this.isZero()) {
        return Kotlin.Long.ZERO;
      } else if (other.isZero()) {
        return Kotlin.Long.ZERO;
      }

      if (this.equals(Kotlin.Long.MIN_VALUE)) {
        return other.isOdd() ? Kotlin.Long.MIN_VALUE : Kotlin.Long.ZERO;
      } else if (other.equals(Kotlin.Long.MIN_VALUE)) {
        return this.isOdd() ? Kotlin.Long.MIN_VALUE : Kotlin.Long.ZERO;
      }

      if (this.isNegative()) {
        if (other.isNegative()) {
          return this.negate().multiply(other.negate());
        } else {
          return this.negate().multiply(other).negate();
        }
      } else if (other.isNegative()) {
        return this.multiply(other.negate()).negate();
      }

      // If both longs are small, use float multiplication
      if (this.lessThan(Kotlin.Long.TWO_PWR_24_) &&
          other.lessThan(Kotlin.Long.TWO_PWR_24_)) {
        return Kotlin.Long.fromNumber(this.toNumber() * other.toNumber());
      }

      // Divide each long into 4 chunks of 16 bits, and then add up 4x4 products.
      // We can skip products that would overflow.

      var a48 = this.high_ >>> 16;
      var a32 = this.high_ & 0xFFFF;
      var a16 = this.low_ >>> 16;
      var a00 = this.low_ & 0xFFFF;

      var b48 = other.high_ >>> 16;
      var b32 = other.high_ & 0xFFFF;
      var b16 = other.low_ >>> 16;
      var b00 = other.low_ & 0xFFFF;

      var c48 = 0, c32 = 0, c16 = 0, c00 = 0;
      c00 += a00 * b00;
      c16 += c00 >>> 16;
      c00 &= 0xFFFF;
      c16 += a16 * b00;
      c32 += c16 >>> 16;
      c16 &= 0xFFFF;
      c16 += a00 * b16;
      c32 += c16 >>> 16;
      c16 &= 0xFFFF;
      c32 += a32 * b00;
      c48 += c32 >>> 16;
      c32 &= 0xFFFF;
      c32 += a16 * b16;
      c48 += c32 >>> 16;
      c32 &= 0xFFFF;
      c32 += a00 * b32;
      c48 += c32 >>> 16;
      c32 &= 0xFFFF;
      c48 += a48 * b00 + a32 * b16 + a16 * b32 + a00 * b48;
      c48 &= 0xFFFF;
      return Kotlin.Long.fromBits((c16 << 16) | c00, (c48 << 16) | c32);
    };


    /**
     * Returns this Long divided by the given one.
     * @param {Kotlin.Long} other Long by which to divide.
     * @return {!Kotlin.Long} This Long divided by the given one.
     */
    Kotlin.Long.prototype.div = function(other) {
      if (other.isZero()) {
        throw Error('division by zero');
      } else if (this.isZero()) {
        return Kotlin.Long.ZERO;
      }

      if (this.equals(Kotlin.Long.MIN_VALUE)) {
        if (other.equals(Kotlin.Long.ONE) ||
            other.equals(Kotlin.Long.NEG_ONE)) {
          return Kotlin.Long.MIN_VALUE;  // recall that -MIN_VALUE == MIN_VALUE
        } else if (other.equals(Kotlin.Long.MIN_VALUE)) {
          return Kotlin.Long.ONE;
        } else {
          // At this point, we have |other| >= 2, so |this/other| < |MIN_VALUE|.
          var halfThis = this.shiftRight(1);
          var approx = halfThis.div(other).shiftLeft(1);
          if (approx.equals(Kotlin.Long.ZERO)) {
            return other.isNegative() ? Kotlin.Long.ONE : Kotlin.Long.NEG_ONE;
          } else {
            var rem = this.subtract(other.multiply(approx));
            var result = approx.add(rem.div(other));
            return result;
          }
        }
      } else if (other.equals(Kotlin.Long.MIN_VALUE)) {
        return Kotlin.Long.ZERO;
      }

      if (this.isNegative()) {
        if (other.isNegative()) {
          return this.negate().div(other.negate());
        } else {
          return this.negate().div(other).negate();
        }
      } else if (other.isNegative()) {
        return this.div(other.negate()).negate();
      }

      // Repeat the following until the remainder is less than other:  find a
      // floating-point that approximates remainder / other *from below*, add this
      // into the result, and subtract it from the remainder.  It is critical that
      // the approximate value is less than or equal to the real value so that the
      // remainder never becomes negative.
      var res = Kotlin.Long.ZERO;
      var rem = this;
      while (rem.greaterThanOrEqual(other)) {
        // Approximate the result of division. This may be a little greater or
        // smaller than the actual value.
        var approx = Math.max(1, Math.floor(rem.toNumber() / other.toNumber()));

        // We will tweak the approximate result by changing it in the 48-th digit or
        // the smallest non-fractional digit, whichever is larger.
        var log2 = Math.ceil(Math.log(approx) / Math.LN2);
        var delta = (log2 <= 48) ? 1 : Math.pow(2, log2 - 48);

        // Decrease the approximation until it is smaller than the remainder.  Note
        // that if it is too large, the product overflows and is negative.
        var approxRes = Kotlin.Long.fromNumber(approx);
        var approxRem = approxRes.multiply(other);
        while (approxRem.isNegative() || approxRem.greaterThan(rem)) {
          approx -= delta;
          approxRes = Kotlin.Long.fromNumber(approx);
          approxRem = approxRes.multiply(other);
        }

        // We know the answer can't be zero... and actually, zero would cause
        // infinite recursion since we would make no progress.
        if (approxRes.isZero()) {
          approxRes = Kotlin.Long.ONE;
        }

        res = res.add(approxRes);
        rem = rem.subtract(approxRem);
      }
      return res;
    };


    /**
     * Returns this Long modulo the given one.
     * @param {Kotlin.Long} other Long by which to mod.
     * @return {!Kotlin.Long} This Long modulo the given one.
     */
    Kotlin.Long.prototype.modulo = function(other) {
      return this.subtract(this.div(other).multiply(other));
    };


    /** @return {!Kotlin.Long} The bitwise-NOT of this value. */
    Kotlin.Long.prototype.not = function() {
      return Kotlin.Long.fromBits(~this.low_, ~this.high_);
    };


    /**
     * Returns the bitwise-AND of this Long and the given one.
     * @param {Kotlin.Long} other The Long with which to AND.
     * @return {!Kotlin.Long} The bitwise-AND of this and the other.
     */
    Kotlin.Long.prototype.and = function(other) {
      return Kotlin.Long.fromBits(this.low_ & other.low_,
                                     this.high_ & other.high_);
    };


    /**
     * Returns the bitwise-OR of this Long and the given one.
     * @param {Kotlin.Long} other The Long with which to OR.
     * @return {!Kotlin.Long} The bitwise-OR of this and the other.
     */
    Kotlin.Long.prototype.or = function(other) {
      return Kotlin.Long.fromBits(this.low_ | other.low_,
                                     this.high_ | other.high_);
    };


    /**
     * Returns the bitwise-XOR of this Long and the given one.
     * @param {Kotlin.Long} other The Long with which to XOR.
     * @return {!Kotlin.Long} The bitwise-XOR of this and the other.
     */
    Kotlin.Long.prototype.xor = function(other) {
      return Kotlin.Long.fromBits(this.low_ ^ other.low_,
                                     this.high_ ^ other.high_);
    };


    /**
     * Returns this Long with bits shifted to the left by the given amount.
     * @param {number} numBits The number of bits by which to shift.
     * @return {!Kotlin.Long} This shifted to the left by the given amount.
     */
    Kotlin.Long.prototype.shiftLeft = function(numBits) {
      numBits &= 63;
      if (numBits == 0) {
        return this;
      } else {
        var low = this.low_;
        if (numBits < 32) {
          var high = this.high_;
          return Kotlin.Long.fromBits(
              low << numBits,
              (high << numBits) | (low >>> (32 - numBits)));
        } else {
          return Kotlin.Long.fromBits(0, low << (numBits - 32));
        }
      }
    };


    /**
     * Returns this Long with bits shifted to the right by the given amount.
     * @param {number} numBits The number of bits by which to shift.
     * @return {!Kotlin.Long} This shifted to the right by the given amount.
     */
    Kotlin.Long.prototype.shiftRight = function(numBits) {
      numBits &= 63;
      if (numBits == 0) {
        return this;
      } else {
        var high = this.high_;
        if (numBits < 32) {
          var low = this.low_;
          return Kotlin.Long.fromBits(
              (low >>> numBits) | (high << (32 - numBits)),
              high >> numBits);
        } else {
          return Kotlin.Long.fromBits(
              high >> (numBits - 32),
              high >= 0 ? 0 : -1);
        }
      }
    };


    /**
     * Returns this Long with bits shifted to the right by the given amount, with
     * zeros placed into the new leading bits.
     * @param {number} numBits The number of bits by which to shift.
     * @return {!Kotlin.Long} This shifted to the right by the given amount, with
     *     zeros placed into the new leading bits.
     */
    Kotlin.Long.prototype.shiftRightUnsigned = function(numBits) {
      numBits &= 63;
      if (numBits == 0) {
        return this;
      } else {
        var high = this.high_;
        if (numBits < 32) {
          var low = this.low_;
          return Kotlin.Long.fromBits(
              (low >>> numBits) | (high << (32 - numBits)),
              high >>> numBits);
        } else if (numBits == 32) {
          return Kotlin.Long.fromBits(high, 0);
        } else {
          return Kotlin.Long.fromBits(high >>> (numBits - 32), 0);
        }
      }
    };

    // Support for Kotlin
    Kotlin.Long.prototype.equalsSafe = function (other) {
        return other instanceof Kotlin.Long && this.equals(other);
    };
})();
