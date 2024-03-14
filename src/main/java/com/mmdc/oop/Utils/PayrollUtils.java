package com.mmdc.oop.Utils;

public class PayrollUtils {
  public static double computePAGIBIG(double grossPay) {
    if (grossPay <= 1500) {
      return (grossPay * 0.01) / 4;
    } else {
      return (grossPay * 0.02) / 4;
    }
  }

  public static double computeSSS(double basic) {
    if (basic < 3250) {
      return 135 / 4;
    }

    if (basic >= 24751) {
      return 1125 / 4;
    }

    double rate = 157.5;
    double charge = 0;
    for (double i = 3250; i < 24751; i += 500) {
      if (basic >= i && basic < i + 500) {
        charge = rate;
        break;
      }
      rate += 22.5;
    }
    return charge / 4;
  }

  public static double computePhilhealth(double basic) {
    double base = (basic * 0.03) / 2;
    if (base <= 300) {
      return 300 / 4;
    } else if (base >= 1800) {
      return 1800 / 4;
    } else {
      return base / 4;
    }
  }

  public static double computeTax(double basic, double pagibig, double phealth, double sss) {
    double deductions = (pagibig * 4) + (phealth * 4) + (sss * 4);
    double taxable = basic - deductions;
    double tax = 0;

    if (taxable <= 20832) {
      return 0;
    } else if (taxable > 20832 && taxable < 33333) {
      tax = (taxable - 20833) * .02;
    } else if (taxable >= 33333 && taxable < 66667) {
      tax = (taxable - 33333) * .25 + 2500;
    } else if (taxable >= 66667 && taxable < 166667) {
      tax = (taxable - 66667) * .3 + 10883;
    } else if (taxable >= 166667 && taxable < 666667) {
      tax = (taxable - 166667) * .32 + 40833.33;
    } else {
      tax = (taxable - 666667) * .35 + 200833.33;
    }
    return tax / 4;
  }

  public static double computeOvertimePay(double totalHours, Double hourlyRate) {
    return (totalHours - 8) * (hourlyRate * 1.5);
  }
}
