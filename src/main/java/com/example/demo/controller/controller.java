package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.TableEntity;
import com.example.demo.service.dbService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;


@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://github.com/ethanlim04/creditcardform")

public class controller {

    private final dbService db;

    @Autowired
    public controller(dbService db) {
        this.db = db;
    }

    @PostMapping("/processInput")
    public ResponseEntity<String> processInput(@RequestBody FormData data) {
        System.out.println("Recieved Process Request");
        db.writeDb(data);
        return ResponseEntity.ok("Processed Request");
    }

    @PostMapping("/updateInput")
    public ResponseEntity<String> updateInput(@RequestBody FormData data) {
        System.out.println("Recieved Update Request");
        System.out.println(data.phone);

        db.updateDb(getID(data.phone), data);
        return ResponseEntity.ok("Updated Request");
    }


    @PostMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(HttpServletResponse response, @RequestBody String phone) {
        System.out.println("Recieved Download");
        System.out.println(phone);

        TableEntity res = db.getDb(getID(phone));

        System.out.println(res.card_midname);


        StreamingResponseBody streamResponseBody = outputStream -> {

        // Get filename from database using fileId from the request
        String filePathName = "g-1450.pdf";

        // Loading editable pdf file
        File input = new File(filePathName);
        try (PDDocument pdfDoc = Loader.loadPDF(input);) {
            pdfDoc.setAllSecurityToBeRemoved(true);

            // Accessing form fields
            PDDocumentCatalog docCatalog = pdfDoc.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (PDField f : acroForm.getFields()) {    

                PDNonTerminalField PDroot = (PDNonTerminalField) f;
                for (PDField childField : PDroot.getChildren()) {

                    PDNonTerminalField PDlv2 = (PDNonTerminalField) childField;
                    for (PDField childFieldlv2 : PDlv2.getChildren()) {

                        String FieldName = childFieldlv2.getPartialName();
                        String formValue = "";

                        if(FieldName.contains("FamilyName[0]")) {
                            formValue = res.lastname;
                            if(FieldName.contains("CCHolderFamilyName[0]")) {
                                formValue = res.card_lastname;
                            }
                        }
                        else if(FieldName.contains("GivenName[0]")) {
                            formValue = res.firstname;
                            if(FieldName.contains("CCHolderGivenName[0]")) {
                                formValue = res.card_firstname;
                            }
                        }
                        else if(FieldName.contains("MiddleName[0]")) {
                            formValue = res.midname;
                            if(FieldName.contains("CCHolderMiddleName[0]")) {
                                formValue = res.card_midname;
                            }
                        }
                        else if(FieldName.contains("Pt1Line2b_StreetNumberName[0]")) {
                            formValue = res.addr;
                        }
                        else if(FieldName.contains("CCHolderAptSteFlrNumber[0]")) {
                            childFieldlv2.setValue(res.addr_num);
                            continue;
                        }
                        else if(FieldName.contains("CityOrTown[0]")) {
                            formValue = res.city;
                        }
                        else if(FieldName.contains("State[0]")) {
                            formValue = res.state;
                        }
                        else if(FieldName.contains("ZipCode[0]")) {
                            formValue = res.zip;
                        }
                        else if(FieldName.contains("DaytimeTelephoneNumber[0]")) {
                            formValue = res.phone;
                        }
                        else if(FieldName.contains("Email[0]")) {
                            formValue = res.email;
                        }
                        
                        else if(FieldName.contains("ExpirationDate[0]")) {
                            // formValue = res.exp;
                            formValue = res.exp.substring(5, 7) + "/" + res.exp.substring(0, 4);
                        }
                        else if(FieldName.contains("AuthorizedPaymentAmt[0]")) {
                            childFieldlv2.setValue(res.authorized_payment);
                            continue;
                        }
                        else if(FieldName.contains("CreditCardTypeChBx[0]") && res.card_type.contains("Visa")) {
                            childFieldlv2.setValue("V");
                            continue;
                        }
                        else if(FieldName.contains("CreditCardTypeChBx[1]") && res.card_type.contains("MasterCard")) {
                            childFieldlv2.setValue("MC");
                            continue;
                        }
                        else if(FieldName.contains("CreditCardTypeChBx[2]") && res.card_type.contains("American Express")) {
                            childFieldlv2.setValue("A");
                            continue;
                        }
                        else if(FieldName.contains("CreditCardTypeChBx[3]") && res.card_type.contains("Discover")) {
                            childFieldlv2.setValue("D");
                            continue;
                        }
                        else if(FieldName.contains("CCHolderAptSteFlr_Unit[0]") && res.addr_type.contains("ste")) {
                            childFieldlv2.setValue(" STE ");
                            continue;
                        }
                        else if(FieldName.contains("CCHolderAptSteFlr_Unit[1]") && res.addr_type.contains("apt")) {
                            childFieldlv2.setValue(" APT ");
                            continue;
                        }
                        else if(FieldName.contains("CCHolderAptSteFlr_Unit[2]") && res.addr_type.contains("flr")) {
                            childFieldlv2.setValue(" FLR ");
                            continue;
                        }
                        else if(FieldName.contains("CreditCardNumber_1[0]")) {
                            childFieldlv2.setValue(res.card_num.substring(0, 4));
                            continue;
                        }
                        else if(FieldName.contains("CreditCardNumber_2[0]")) {
                            childFieldlv2.setValue(res.card_num.substring(4, 8));
                            continue;
                        }
                        else if(FieldName.contains("CreditCardNumber_3[0]")) {
                            childFieldlv2.setValue(res.card_num.substring(8, 12));
                            continue;
                        }
                        else if(FieldName.contains("CreditCardNumber_4[0]")) {
                            childFieldlv2.setValue(res.card_num.substring(12, 16));
                            continue;
                        }
                        else {
                            System.out.println("AAA");
                            System.out.println(FieldName);
                            continue;
                        }

                        System.out.println(FieldName);
                        System.out.println(formValue);
                        childFieldlv2.setValue(formValue);
                    }
                }
            };

            /* make the final document uneditable */
            acroForm.flatten();
            pdfDoc.save(out);

            // Getting the size of the file from ByteArrayOutputStream
            long fileLength = out.size();
            response.setContentLength((int) fileLength);
            response.setHeader("Content-Disposition", "attachment; filename=g-1450.pdf");

            // Write the content of the ByteArrayOutputStream to the outputStream
            out.writeTo(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        };


        return ResponseEntity.ok(streamResponseBody);
    }

    @PostMapping("/user")
    public ResponseEntity<TableEntity> user(@RequestBody String phone) {
        System.out.println("Recieved Request");
        System.out.println(phone);

        TableEntity res = db.getDb(getID(phone));
        return ResponseEntity.ok(res);
    }

    
    private long getID(String s) {
        long res = 0;
        int length = 9;
        for(char c : s.toCharArray()) {
            // res += (int) c;
            if(c != '(' || c != ')' || c != ' ') {
                int num = (c - '0'); //convert phone to decimal
                res += num * Math.pow(10, length);
                length--;
            }
        }
        return res;
    }
}