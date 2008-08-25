/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.php
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * Provider.java
 *
 * Created on May 15, 2007, 11:59 AM
 *
 */

package javax.ws.rs.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an implementation of {@link ExceptionMapper}, {@link ContextResolver},
 * {@link MessageBodyReader} or {@link MessageBodyWriter}. An annotated class 
 * MUST implement one or more of these interfaces.
 * 
 * @see MessageBodyReader
 * @see MessageBodyWriter
 * @see ContextResolver
 * @see ExceptionMapper
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Provider {
    
}
