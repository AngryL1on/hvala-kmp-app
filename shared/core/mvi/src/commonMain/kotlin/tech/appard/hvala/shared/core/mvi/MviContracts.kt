package tech.appard.hvala.shared.core.mvi

/**
 * Marker for screen state in MVI.
 */
interface MviState

/**
 * Marker for user actions / events in MVI.
 */
interface MviIntent

/**
 * Marker for one-shot side effects (navigation, snackbars, etc.).
 */
interface MviEffect
