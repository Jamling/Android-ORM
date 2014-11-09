/*
 * Copyright (C) 2007 The Android Open Source Project
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

package android.net;

/**
 * Immutable URI reference. A URI reference includes a URI and a fragment, the
 * component of the URI following a '#'. Builds and parses URI references which
 * conform to <a href="http://www.faqs.org/rfcs/rfc2396.html">RFC 2396</a>.
 * 
 * <p>
 * In the interest of performance, this class performs little to no validation.
 * Behavior is undefined for invalid input. This class is very forgiving--in the
 * face of invalid input, it will return garbage rather than throw an exception
 * unless otherwise specified.
 */
public abstract class Uri {
    
}
