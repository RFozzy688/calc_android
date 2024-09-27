package itstep.learning.spu221;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalcActivity extends AppCompatActivity {
    private final int maxDigits = 13;
    private TextView tvHistory;
    private TextView tvResult;
    private String zeroSign;
    private String dotSign;
    private String minusSign;
    private boolean needClearResult;
    private String operatorSign;
    private double operand1;
    private double operand2;

    @SuppressLint("DiscouragedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calc_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvHistory = findViewById(R.id.calc_tv_history);
        tvResult = findViewById(R.id.calc_tv_result);
        zeroSign = getString(R.string.calc_btn_0);
        dotSign = getString(R.string.calc_btn_dot);
        minusSign = getString(R.string.calc_minus_sign);

        findViewById(R.id.calc_btn_C).setOnClickListener(this::clearClick);
        findViewById(R.id.calc_btn_dot).setOnClickListener(this::dotClick);
        findViewById(R.id.calc_btn_plus_minus).setOnClickListener(this::pmClick);
        findViewById(R.id.calc_btn_back).setOnClickListener(this::backspaceClick);
        findViewById(R.id.calc_btn_square_root).setOnClickListener(this::squareRootClick);
        findViewById(R.id.calc_btn_1_x).setOnClickListener(this::inverseProportionalityClick);
        findViewById(R.id.calc_btn_pow).setOnClickListener(this::squareClick);
        findViewById(R.id.calc_btn_divide).setOnClickListener(this::divideClick);
        findViewById(R.id.calc_btn_plus).setOnClickListener(this::plusClick);
        findViewById(R.id.calc_btn_multiply).setOnClickListener(this::multiplyClick);
        findViewById(R.id.calc_btn_difference).setOnClickListener(this::differenceClick);
        findViewById(R.id.calc_btn_equals).setOnClickListener(this::equalsClick);

        for (int i = 0; i < 10; i++) {
            findViewById(
                    //R.id.calc_btn_C
                    getResources()  // R
                    .getIdentifier( // id
                            "calc_btn_" + i,
                            "id",
                            getPackageName()
                    )
            ).setOnClickListener(this::digitClick);
        }

        if(savedInstanceState == null) {
            this.clearClick(null);
            needClearResult = false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("savedResult", tvResult.getText());
        outState.putBoolean("needClearResult", needClearResult);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvResult.setText(savedInstanceState.getCharSequence("savedResult"));
        needClearResult = savedInstanceState.getBoolean("needClearResult");
    }

    private void inverseProportionalityClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);

        double x = Double.parseDouble(res);

        showResult(1 / x);

        res = new String(Character.toChars(0x215F)) + "(" + res + ")";
        tvHistory.setText(res);
    }

    private void squareClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);

        double x = Double.parseDouble(res);

        showResult(x * x);

        res = "(" + res + ")" + new String(Character.toChars(0x00B2));
        tvHistory.setText(res);
    }

    private void squareRootClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);

        double x = Double.parseDouble(res);
        if (x < 0) {
            tvResult.setText(R.string.calc_text_invalid_input);
        }
        else {
            showResult(Math.sqrt(x));
        }

        res = new String(Character.toChars(0x221A)) + "(" + res + ")";
        tvHistory.setText(res);
    }

    private void divideClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);
        operand1 = Double.parseDouble(res);
        operatorSign = getString(R.string.calc_btn_divide);
        needClearResult = true;
        res += " " + operatorSign;
        tvHistory.setText(res);
    }

    private void multiplyClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);
        operand1 = Double.parseDouble(res);
        operatorSign = getString(R.string.calc_btn_multiply);
        needClearResult = true;
        res += " " + operatorSign;
        tvHistory.setText(res);
    }

    private void differenceClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);
        operand1 = Double.parseDouble(res);
        operatorSign = getString(R.string.calc_btn_difference);
        needClearResult = true;
        res += " " + operatorSign;
        tvHistory.setText(res);
    }

    private void plusClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);
        operand1 = Double.parseDouble(res);
        operatorSign = getString(R.string.calc_btn_plus);
        needClearResult = true;
        res += " " + operatorSign;
        tvHistory.setText(res);
    }

    private void equalsClick(View view) {
        String res = tvResult.getText().toString();
        res = parseText(res);
        operand2 = Double.parseDouble(res);

        String history = tvHistory.getText().toString();
        history += " " + res + " =";
        tvHistory.setText(history);

        switch (operatorSign)
        {
            case "÷":
            {
                if (operand2 == 0) {
                    tvResult.setText(R.string.calc_text_divide_by_zero);
                    break;
                }
                showResult(operand1 / operand2);
            }
            break;
            case "×":
                showResult(operand1 * operand2);
                break;
            case "−":
                showResult(operand1 - operand2);
                break;
            case "+":
                showResult(operand1 + operand2);
                break;
        }

    }

    private String parseText(String str) {
        return str
                .replace(zeroSign, "0")
                .replace(minusSign, "-")
                .replace(dotSign, ".");
    }

    private void showResult(double x) {
        needClearResult = true;
        if (x >= 1e13 || x <= -1e13) {
            tvResult.setText(R.string.calc_text_overflow);
            return;
        }
        String res = (x == (int)x) ? String.valueOf((int) x) : String.valueOf(x);
        res = res
                .replace("0", zeroSign)
                .replace("-", minusSign)
                .replace(".", dotSign);
        int limit = maxDigits;
        if (res.startsWith(minusSign)){
            limit += 1;
        }
        if (res.contains(dotSign)) {
            limit += 1;
        }
        if (res.length() > limit) {
            res = res.substring(0, limit);
        }
        tvResult.setText(res);
    }

    private void clearClick(View view) {
        tvHistory.setText("");
        tvResult.setText(zeroSign);
    }

    private void digitClick(View view) {
        String res = needClearResult ? "" : tvResult.getText().toString();
        needClearResult = false;
        if (digitLength(res) >= maxDigits) {
            Toast.makeText(this, R.string.calc_msg_max_digits, Toast.LENGTH_SHORT).show();
            return;
        }

        if(zeroSign.equals(res)){
            res = "";
        }
        res += ((Button)view).getText();
        tvResult.setText(res);
    }

    private void dotClick(View view) {
        String res = needClearResult ? "0" : tvResult.getText().toString();
        needClearResult = false;
        if (res.contains(dotSign)){
            Toast.makeText(this, R.string.calc_msg_two_dots, Toast.LENGTH_SHORT).show();
        }
        else {
            res += dotSign;
            tvResult.setText(res);
        }
    }

    private void pmClick(View view) {
        String res = tvResult.getText().toString();
        if (res.startsWith(minusSign)) {
            res = res.substring(minusSign.length());
        }
        else if (!zeroSign.equals(res)){
            res = minusSign + res;
        }
        tvResult.setText(res);
    }

    private void backspaceClick(View view) {
        String res = tvResult.getText().toString();
        int len = res.length();

        if (len > 1) {
            res = res.substring(0, len - 1);
            if(minusSign.equals(res)){
                res = zeroSign;
            }
        }
        else {
            res = zeroSign;
        }
        tvResult.setText(res);
    }

    private int digitLength(String input){
        int ret = input.length();

        if (input.startsWith(minusSign)){
            ret -= 1;
        }
        if (input.contains(dotSign)){
            ret -= 1;
        }

        return ret;
    }
}