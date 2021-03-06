package org.helgoboss.domino

import bundle_watching.BundleWatching
import configuration_watching.ConfigurationWatching
import logging.Logging
import org.helgoboss.capsule._
import service_consuming.ServiceConsuming
import service_providing.ServiceProviding
import service_watching.ServiceWatching

/**
 * This is the main entry point to the Domino DSL. 
 *
 * By having your bundle activator extend from this class, you get '''full''' access to the Domino DSL.
 * In most cases [[DominoActivator]] is all you need because it mixes in all the other functionality.
 * 
 * Note that if you use [[watchServices]] or [[watchBundles]], you might additionally want to import the
 * relevant watcher events.
 *
 * 
 *
 * == Example 1: Wait for a service ==
 *
 * {{{
 *   package org.example.domino_test_one
 * 
 *   import org.helgoboss.domino.DominoActivator
 *   import org.osgi.service.http.HttpService
 *
 *   class MyService(httpService: HttpService)
 *
 *   class Activator extends DominoActivator {
 *     whenBundleActive {
 *       // Make MyService available as long as HttpService is present
 *       whenServicePresent[HttpService] { httpService =>
 *         val myService = new MyService(httpService)
 *         myService.providesService[MyService]
 *       }
 *     }
 *   }
 * }}}
 *
 * == Example 2: Listen for configuration changes ==
 *
 * {{{
 *   package org.example.domino_test_two
 * 
 *   import org.helgoboss.domino.DominoActivator
 *
 *   class KeyService(key: String)
 *
 *   class Activator extends DominoActivator {
 *     whenBundleActive {
 *       // Reregister KeyService whenever configuration changes
 *       whenConfigurationActive("my_service") { conf =>
 *         val key = conf.get("key") map { _.asInstanceOf[String] } getOrElse "defaultKey"
 *         new KeyService(key).providesService[KeyService]
 *       }
 *     }
 *   }
 * }}}
 *
 * @constructor Will be called by the OSGi framework.
 * @note I suggest extending from this class instead of mixing in the subpackage traits. Then you don't need to
 *       recompile your bundle if minor internal changes are made to Domino. This results in greater upwards compatibility.
 */
abstract class DominoActivator extends OsgiContext
    with CapsuleConvenience
    with BundleWatching
    with ConfigurationWatching
    with ServiceConsuming
    with Logging
    with ServiceProviding
    with ServiceWatching {
  
  /** Dependency */
  protected val capsuleContext = this
  
  /** Dependency */
  protected val serviceProviding = this
  
  /** Dependency */
  protected val serviceConsuming = this
}