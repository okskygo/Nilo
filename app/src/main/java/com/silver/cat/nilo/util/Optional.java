package com.silver.cat.nilo.util;

import java.io.Serializable;
import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public abstract class Optional<T> implements Serializable {
  private static final class Absent extends Optional<Object> {
    private static final Absent INSTANCE = new Absent();

    private static final long serialVersionUID = 0;

    @Override
    public boolean equals(final Object object) {
      return object == this;
    }

    @Override
    public Object get() {
      throw new IllegalStateException("value is absent");
    }

    @Override
    public int hashCode() {
      return 0x598df91c;
    }

    @Override
    public boolean isPresent() {
      return false;
    }

    private Object readResolve() {
      return INSTANCE;
    }

    @Override
    public String toString() {
      return "Optional.absent()";
    }

    @Override
    public Optional<Object> filter(Predicate<? super Object> predicate) {
      return INSTANCE;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U> Optional<U> map(Function<? super Object, ? extends U> mapper) {
      return (Optional<U>) INSTANCE;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U> Optional<U> flatMap(Function<? super Object, Optional<U>> mapper) {
      return (Optional<U>) INSTANCE;
    }

    @Override
    public void ifPresent(Consumer<? super Object> consumer) {
    }

    @Override
    public Object orElse(Object other) {
      return other;
    }

    @Override
    public Object orElseGet(Callable<Object> supplier) {
      try {
        return supplier.call();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public <X extends Throwable> Object orElseThrow(Callable<X> supplier) throws X {
      final X exception;
      try {
        exception = supplier.call();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
      throw exception;
    }
  }

  private static final class Present<T> extends Optional<T> {
    private static final long serialVersionUID = 0;
    private final T reference;

    Present(final T reference) {
      this.reference = reference;
    }

    @Override
    public boolean equals(final Object object) {
      if (object instanceof Present) {
        final Present<?> other = (Present<?>) object;
        return reference.equals(other.reference);
      }
      return false;
    }

    @Override
    public T get() {
      return reference;
    }

    @Override
    public int hashCode() {
      return 0x598df91c + reference.hashCode();
    }

    @Override
    public boolean isPresent() {
      return true;
    }

    @Override
    public String toString() {
      return "Optional.of(" + reference + ")";
    }

    @Override
    public Optional<T> filter(Predicate<? super T> predicate) {
      try {
        final boolean pass = predicate.test(get());
        if (pass) {
          return this;
        } else {
          return empty();
        }
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
      try {
        return ofNullable(mapper.apply(get()));
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
      final Optional<U> result;
      try {
        result = mapper.apply(get());
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }

      return checkNotNull(result);
    }

    @Override
    public void ifPresent(Consumer<? super T> consumer) {
      try {
        consumer.accept(get());
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public T orElse(T other) {
      return get();
    }

    @Override
    public T orElseGet(Callable<T> supplier) {
      return get();
    }

    @Override
    public <X extends Throwable> T orElseThrow(Callable<X> supplier) throws X {
      return get();
    }
  }

  private static final long serialVersionUID = 0;

  /**
   * Returns an {@code Optional} instance with no contained reference.
   */
  @SuppressWarnings("unchecked")
  public static <T> Optional<T> empty() {
    return (Optional<T>) Absent.INSTANCE;
  }

  private static <T> T checkNotNull(final T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  private static <T> T checkNotNull(final T reference, final Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  /**
   * If {@code nullableReference} is non-null, returns an {@code Optional} instance containing
   * that reference; otherwise returns {@link Optional#empty}.
   */
  public static <T> Optional<T> ofNullable(final T nullableReference) {
    return (nullableReference == null) ? Optional.<T>empty() : new Present<T>(nullableReference);
  }

  /**
   * Returns an {@code Optional} instance containing the given non-null reference.
   */
  public static <T> Optional<T> of(final T reference) {
    return new Present<T>(checkNotNull(reference));
  }

  private Optional() {
  }

  /**
   * Returns {@code true} if {@code object} is an {@code Optional} instance, and either the
   * contained references are {@linkplain Object#equals equal} to each other or both are absent.
   * Note that {@code Optional} instances of differing parameterized types can be equal.
   */
  @Override
  public abstract boolean equals(Object object);

  /**
   * Returns the contained instance, which must be present. If the instance might be absent, use
   * {@link #orElse(Object)} instead.
   *
   * @throws IllegalStateException
   *     if the instance is absent ({@link #isPresent} returns {@code false})
   */
  public abstract T get();

  /**
   * Returns a hash code for this instance.
   */
  @Override
  public abstract int hashCode();

  /**
   * Returns {@code true} if this holder contains a (non-null) instance.
   */
  public abstract boolean isPresent();

  /**
   * Returns a string representation for this instance. The form of this string representation is
   * unspecified.
   */
  @Override
  public abstract String toString();

  /**
   * If a value is present, and the value matches the given predicate,
   * return an {@code Optional} describing the value, otherwise return an
   * empty {@code Optional}.
   *
   * @param predicate
   *     a predicate to apply to the value, if present
   * @return an {@code Optional} describing the value of this {@code Optional}
   * if a value is present and the value matches the given predicate,
   * otherwise an empty {@code Optional}
   */
  public abstract Optional<T> filter(Predicate<? super T> predicate);

  /**
   * If a value is present, apply the provided mapping function to it,
   * and if the result is non-null, return an {@code Optional} describing the
   * result.  Otherwise return an empty {@code Optional}.
   *
   * @param <U>
   *     The type of the result of the mapping function
   * @param mapper
   *     a mapping function to apply to the value, if present
   * @return an {@code Optional} describing the result of applying a mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   */
  public abstract <U> Optional<U> map(Function<? super T, ? extends U> mapper);

  /**
   * If a value is present, apply the provided {@code Optional}-bearing
   * mapping function to it, return that result, otherwise return an empty
   * {@code Optional}.  This method is similar to {@link #map(Function)},
   * but the provided mapper is one whose result is already an {@code Optional},
   * and if invoked, {@code flatMap} does not wrap it with an additional
   * {@code Optional}.
   *
   * @param <U>
   *     The type parameter to the {@code Optional} returned by
   * @param mapper
   *     a mapping function to apply to the value, if present
   *     the mapping function
   * @return the result of applying an {@code Optional}-bearing mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   */
  public abstract <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper);

  /**
   * If a value is present, invoke the specified consumer with the value,
   * otherwise do nothing.
   *
   * @param consumer
   *     block to be executed if a value is present
   */
  public abstract void ifPresent(Consumer<? super T> consumer);

  /**
   * Return the value if present, otherwise return {@code other}.
   *
   * @param other
   *     the value to be returned if there is no value present, may
   *     be null
   * @return the value, if present, otherwise {@code other}
   */
  public abstract T orElse(T other);

  /**
   * Return the value if present, otherwise invoke {@code other} and return
   * the result of that invocation.
   *
   * @param other
   *     a {@code Callable} whose result is returned if no value
   *     is present
   * @return the value if present otherwise the result of {@code other.get()}
   */
  public abstract T orElseGet(Callable<T> other);

  /**
   * Return the contained value, if present, otherwise throw an exception
   * to be created by the provided supplier.
   *
   * @param <X>
   *     Type of the exception to be thrown
   * @param exceptionSupplier
   *     The supplier which will return the exception to
   *     be thrown
   * @return the present value
   * @throws X
   *     if there is no value present
   */
  public abstract <X extends Throwable> T orElseThrow(Callable<X> exceptionSupplier) throws X;
}