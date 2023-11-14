package com.example.sigma_blue.activities;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This class is the new method in which data is now getting passed between activities. Makes
 * activity result listening much more simple.
 * Citation:
 * <a href="https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative">...</a>
 * @param <Input>
 * @param <Result>
 */
public class ActivityLauncher<Input, Result> {

    /**
     * Factory method for creating an ActivityLauncher object for the activities.
     * @param caller
     * @param contract
     * @param onActivityResult
     * @return
     * @param <Input>
     * @param <Result>
     */
    public static <Input, Result> ActivityLauncher<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract,
            @Nullable OnActivityResult<Result> onActivityResult
    ) {
        return new ActivityLauncher<>(caller, contract, onActivityResult);
    }

    /**
     * Overloaded registerForActivityResult method.
     * @param caller
     * @param contract
     * @return
     * @param <Input>
     * @param <Result>
     */
    @NonNull
    public static <Input, Result> ActivityLauncher<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract
    ) {

        return new ActivityLauncher<>(caller, contract, null);
    }

    /**
     * Specialised method for launching new activities.
     */
    @NonNull
    public static ActivityLauncher<Intent, ActivityResult> registerActivityForResult(
            @NonNull ActivityResultCaller caller) {
        return registerForActivityResult(caller, new ActivityResultContracts.StartActivityForResult());
    }

    /**
     * This is the callback interface
     */
    public interface OnActivityResult<O> {
        void onActivityResult(O result);
    }

    private final ActivityResultLauncher<Input> launcher;
    @Nullable
    private OnActivityResult<Result> onActivityResult;
    private ActivityLauncher(@NonNull ActivityResultCaller caller,
                                  @NonNull ActivityResultContract<Input, Result> contract,
                                  @Nullable OnActivityResult<Result> onActivityResult) {
        this.onActivityResult = onActivityResult;
        this.launcher = caller.registerForActivityResult(contract, this::callOnActivityResult);
    }

    public void setOnActivityResult(@Nullable OnActivityResult<Result> onActivityResult) {
        this.onActivityResult = onActivityResult;
    }
    /**
     * Launch activity, same as {@link ActivityResultLauncher#launch(Object)} except that it allows a callback
     * executed after receiving a result from the target activity.
     */
    public void launch(Input input, @Nullable OnActivityResult<Result> onActivityResult) {
        if (onActivityResult != null) {
            this.onActivityResult = onActivityResult;
        }
        launcher.launch(input);
    }

    /**
     * Same as {@link #launch(Object, OnActivityResult)} with last parameter set to {@code null}.
     */
    public void launch(Input input) {
        launch(input, this.onActivityResult);
    }

    private void callOnActivityResult(Result result) {
        if (onActivityResult != null) onActivityResult.onActivityResult(result);
    }
}
