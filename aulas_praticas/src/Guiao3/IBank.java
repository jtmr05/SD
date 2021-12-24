package guiao3;

interface IBank {

    public int createAccount(int balance);

    public int closeAccount(int id);

    public int balance(int id);

    public boolean deposit(int id, int value);

    public boolean withdraw(int id, int value);

    public boolean transfer(int from, int to, int value);

    public int totalBalance(int[] ids);

    public void debug();
}
