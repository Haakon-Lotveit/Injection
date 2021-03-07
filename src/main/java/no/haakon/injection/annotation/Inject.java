package no.haakon.injection.annotation;

import no.haakon.injection.injector.InjectorWithNaming;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Lar deg be om en innsprøytning på en gitt konstruktør, en konstruktørparameter, eller et felt.
 * Feltinnsprøytning er slikt bøndene gjør om våren og lukter generelt sett like godt på marken som i koden.
 *
 * Annotasjonene her betyr:
 * <ul>
 *     <li><code>@Retention</code> Sier hvor annotasjonen er tilgjengelig.
 *         Før kompilering (Source) men ikke senere,
 *         etter kompilering (Class) men ikke senere,
 *         eller når programmet kjører (Runtime). Forskjellige verktøy kan dra nytte av annotasjonene på ulikt vis.</li>
 *     <li><code>@Target</code> sier hvor du har lov til å putte annotasjonen.
 *         Merk at metoder og konstruktører er forskjellige ting. Her sier vi at den kan puttes på parametre,
 *         konstruktører og felt.</li>
 *     <li>stringmetoden her er spesiell. vanligvis må du sette verdier slik: @Annotasjon(navn = "verdi"), men
 *         value() metoden er spesiell. den lar deg sette verdien slik: @Annotasjon("verdi"), som er lettere.
 *         Merk at typene som er tillatt brukt her er begrenset. (String, Enum, Class og primitive verdier)
 *         Det er flere begrensninger her. Du kan også oppgi et array av verdier. Verdien må være final.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({CONSTRUCTOR, FIELD, PARAMETER})
public @interface Inject {
    String value() default InjectorWithNaming.DEFAULT_NAME;
}
