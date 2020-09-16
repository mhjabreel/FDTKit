
package fdt.utils;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author Najlaa Maaroof
 * @param <T>
 */
public final class Nullable<T> implements Serializable{
    /**
     * Common instance for {@code empty()}.
     */
    private static final Nullable<?> EMPTY = new Nullable<>();

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private final T value;

    /**
     * Constructs an empty instance.
     *
     * @implNote Generally only one empty instance, {@link Nullable#EMPTY},
     * should exist per VM.
     */
    private Nullable() {
        this.value = null;
    }

    /**
     * Returns an empty {@code Nullable} instance.  No value is present for this
     * Nullable.
     *
     * apiNote Though it may be tempting to do so, avoid testing if an object
     * is empty by comparing with {@code ==} against instances returned by
     * {@code Option.empty()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> Type of the non-existent value
     * @return an empty {@code Nullable}
     */
    public static<T> Nullable<T> empty() {
        @SuppressWarnings("unchecked")
        Nullable<T> t = (Nullable<T>) EMPTY;
        return t;
    }

    /**
     * Constructs an instance with the value present.
     *
     * @param value the non-null value to be present
     * @throws NullPointerException if value is null
     */
    private Nullable(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns an {@code Nullable} with the specified present non-null value.
     *
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Nullable} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> Nullable<T> of(T value) {
        return new Nullable<>(value);
    }

    /**
     * Returns an {@code Nullable} describing the specified value, if non-null,
     * otherwise returns an empty {@code Nullable}.
     *
     * @param <T> the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Nullable} with a present value if the specified value
     * is non-null, otherwise an empty {@code Nullable}
     */
    public static <T> Nullable<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * If a value is present in this {@code Nullable}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Nullable}
     * @throws NoSuchElementException if there is no value present
     *
     * @see Nullable#isPresent()
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     * null
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }

    /**
     * If a value is present, and the value matches the given predicate,
     * return an {@code Nullable} describing the value, otherwise return an
     * empty {@code Nullable}.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an {@code Nullable} describing the value of this {@code Nullable}
     * if a value is present and the value matches the given predicate,
     * otherwise an empty {@code Nullable}
     * @throws NullPointerException if the predicate is null
     */
    public Nullable<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent())
            return this;
        else
            return predicate.test(value) ? this : empty();
    }

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an {@code Nullable} describing the
     * result.  Otherwise return an empty {@code Nullable}.
     *
     * apiNote This method supports post-processing on optional values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of file names, selects one that has
     * not yet been processed, and then opens that file, returning an
     * {@code Nullable<FileInputStream>}:
     *
     * <pre>{@code
     *     Nullable<FileInputStream> fis =
     *         names.stream().filter(name -> !isProcessedYet(name))
     *                       .findFirst()
     *                       .map(name -> new FileInputStream(name));
     * }</pre>
     *
     * Here, {@code findFirst} returns an {@code Nullable<String>}, and then
     * {@code map} returns an {@code Nullable<FileInputStream>} for the desired
     * file if one exists.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Nullable} describing the result of applying a mapping
     * function to the value of this {@code Nullable}, if a value is present,
     * otherwise an empty {@code Nullable}
     * @throws NullPointerException if the mapping function is null
     */
    public<U> Nullable<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Nullable.ofNullable(mapper.apply(value));
        }
    }

    /**
     * If a value is present, apply the provided {@code Nullable}-bearing
     * mapping function to it, return that result, otherwise return an empty
     * {@code Nullable}.  This method is similar to {@link #map(Function)},
     * but the provided mapper is one whose result is already an {@code Nullable},
     * and if invoked, {@code flatMap} does not wrap it with an additional
     * {@code Nullable}.
     *
     * @param <U> The type parameter to the {@code Nullable} returned by
     * @param mapper a mapping function to apply to the value, if present
     *           the mapping function
     * @return the result of applying an {@code Nullable}-bearing mapping
     * function to the value of this {@code Nullable}, if a value is present,
     * otherwise an empty {@code Nullable}
     * @throws NullPointerException if the mapping function is null or returns
     * a null result
     */
    public<U> Nullable<U> flatMap(Function<? super T, Nullable<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value
     * is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is
     * null
     */
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * apiNote A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     * be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and
     * {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this Nullable. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Nullable} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Nullable)) {
            return false;
        }

        Nullable<?> other = (Nullable<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Returns a non-empty string representation of this Nullable suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * implSpec If a value is present the result must include its string
     * representation in the result. Empty and present Nullables must be
     * unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return value != null
            ? String.format("Nullable[%s]", value)
            : "Nullable.empty";
    }
}

