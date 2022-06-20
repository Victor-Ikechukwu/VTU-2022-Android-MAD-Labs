// IEmiCalcultorAidlInterface.aidl
package Common;

// Declare any non-default types here with import statements

interface IEmiCalcultorAidlInterface {
 double getCarEMI(double principalAmount, double interestRate, int loanTenure);
}