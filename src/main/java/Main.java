import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Ring Signatures System");

            while (true) {

                System.out.println("Select Your Option: ");
                System.out.println("1 - Sign message");
                System.out.println("2 - Verify signature");
                System.out.println("3 - Exit");
                System.out.println("Insert option here: ");

                String option = reader.readLine();

                while (!tryParseInt(option)) {
                    System.out.println("Please Introduce only the Number of The Option You Select:");
                    option = reader.readLine();
                }

                switch (Integer.valueOf(option)) {
                    case 1:
                        System.out.println("Choose group size: ");
                        option = reader.readLine();

                        while (true) {
                            if(tryParseInt(option)) {
                                if (Integer.valueOf(option) >= 2 && Integer.valueOf(option) <= 9) {
                                    break;
                                }
                            }
                            System.out.println("Please introduce only a number between 2 and 9:");
                            option = reader.readLine();
                        }

                        Manager.groupSetter(Integer.valueOf(option));

                        System.out.println("Choose the member to sign message: ");
                        option = reader.readLine();

                        while (true) {
                            if(tryParseInt(option)) {
                                if (Integer.valueOf(option) >= 1 && Integer.valueOf(option) <= Manager.members.size()) {
                                    break;
                                }
                            }
                            System.out.println("Please introduce only a number between 1 and " + Manager.members.size());
                            option = reader.readLine();
                        }

                        System.out.println("Write the message to be signed: ");
                        String message = reader.readLine();

                        Manager.members.get(Integer.valueOf(option)).sign(message);

                        break;
                    case 2:
                        ExternalEntity.verifySignature();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            System.out.println("The introduced Input could not be converted to an integer.");
            return false;
        }
    }
}
