package com.manager.service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

import com.manager.pojo.ClassGradeInfoView;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class ChaincodeService {

    Gateway.Builder builder;

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public void CreateClassGrade(String classGradeNumber, Integer sid, String studentName, Double grade, Double makeupGrade, String className, String teacherName, Integer credit, Integer rebuild) throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("chaincode");

            contract.submitTransaction("createClassGrade", sid.toString(), grade.toString(), makeupGrade.toString(), className);
        }
    }

    public ClassGradeInfoView QueryClassGrade(String className, Integer sid) throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("chaincode");

            byte[] result;
            result = contract.evaluateTransaction("queryClassGrade", className, sid.toString());
            ByteArrayInputStream bais = null;
            // 反序列化
            bais = new ByteArrayInputStream(result);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (ClassGradeInfoView) ois.readObject();
        }
    }

    public ClassGradeInfoView[] QueryAllClassGrades() throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("chaincode");

            byte[] result;

            result = contract.evaluateTransaction("queryAllClassGrades");
            ByteArrayInputStream bais = null;
            //反序列化
            bais = new ByteArrayInputStream(result);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (ClassGradeInfoView[]) ois.readObject();
        }
    }

    public void ChangeGrade(String className, Integer sid, Double newGrade) throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("chaincode");

            contract.submitTransaction("changeGrade", className, sid.toString(), newGrade.toString());

        }
    }

    public void ChangeMakeupGrade(String className, Integer sid, Double newMakeupGrade) throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("src/main/resources/wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("chaincode");

            contract.submitTransaction("changeMakeupGrade", className, sid.toString(), newMakeupGrade.toString());

        }
    }


//    public static void main(String[] args) throws Exception {
//        // Load a file system based wallet for managing identities.
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//        // load a CCP
//        Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
//
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
//
//        // create a gateway connection
//        try (Gateway gateway = builder.connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("mychannel");
//            Contract contract = network.getContract("fabcar");
//
//            byte[] result;
//
//            result = contract.evaluateTransaction("queryAllCars");
//            System.out.println(new String(result));
//
//            contract.submitTransaction("createCar", "CAR10", "VW", "Polo", "Grey", "Mary");
//
//            result = contract.evaluateTransaction("queryCar", "CAR10");
//            System.out.println(new String(result));
//
//            contract.submitTransaction("changeCarOwner", "CAR10", "Archie");
//
//            result = contract.evaluateTransaction("queryCar", "CAR10");
//            System.out.println(new String(result));
//        }
//    }

}

class EnrollAdmin {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public static void main(String[] args) throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "../test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("src/main/resources/wallet"));

        // Check to see if we've already enrolled the admin user.
        if (wallet.get("admin") != null) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        Identity user = Identities.newX509Identity("Org1MSP", enrollment);
        wallet.put("admin", user);
        System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
    }
}

class RegisterUser {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public static void main(String[] args) throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "../test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("src/main/resources/wallet"));

        // Check to see if we've already enrolled the user.
        if (wallet.get("appUser") != null) {
            System.out.println("An identity for the user \"appUser\" already exists in the wallet");
            return;
        }

        X509Identity adminIdentity = (X509Identity) wallet.get("admin");
        if (adminIdentity == null) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return;
        }
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest("appUser");
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID("appUser");
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll("appUser", enrollmentSecret);
        Identity user = Identities.newX509Identity("Org1MSP", enrollment);
        wallet.put("appUser", user);
        System.out.println("Successfully enrolled user \"appUser\" and imported it into the wallet");
    }

}
