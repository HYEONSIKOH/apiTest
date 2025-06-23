package org.ohs.apitest.domain.index;

import lombok.*;
import org.ohs.apitest.domain.index.repository.IndexRepository;
import org.ohs.apitest.domain.index.repository.NoIndexRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final NoIndexRepository noIndexRepository;
    private final IndexRepository indexRepository;

    public ResponseEntity<?> insertDummyData() {
        if (noIndexRepository.existsByUserData())
            return ResponseEntity.ok().body("User data already exists.");

        String fileName = "data.csv";
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.append("id,name,email\n");

            Random random = new Random();
            int totalRecords = 1_000_000;

            Set<String> emails = new HashSet<>(); // 중복방지 Set
            for (int i = 0; i < totalRecords; i++) {
                String name = "name_" + UUID.randomUUID().toString().substring(0, 8);
                String email;

                do {
                    email = "test_" + random.nextInt(totalRecords) + "@example.com";
                } while(!emails.add(email));

                writer.append(String.format("%d,%s,%s\n", i, name, email));
            }

            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getUserByEmail(String email) {
        long start = System.currentTimeMillis();

        noIndexRepository.findByEmail(email);

        long end = System.currentTimeMillis();
        // System.out.println("조회 시간: " + (end - start) + "ms");

        return ResponseEntity.ok().body("조회 시간: " + (end - start) + "ms");
    }

    public ResponseEntity<?> getUserByEmailIndexVer(String email) {
        long start = System.currentTimeMillis();

        indexRepository.findByEmail(email);

        long end = System.currentTimeMillis();
        // System.out.println("조회 시간(인덱스): " + (end - start) + "ms");

        return ResponseEntity.ok().body("조회 시간(인덱스): " + (end - start) + "ms");
    }

    public ResponseEntity<?> getUserByEmailVer2(String str) {
        long start = System.currentTimeMillis();

        noIndexRepository.findByEmailContaining(str);

        long end = System.currentTimeMillis();
        // System.out.println("조회 시간(인덱스): " + (end - start) + "ms");

        return ResponseEntity.ok().body("조회 시간(인덱스): " + (end - start) + "ms");
    }

    public ResponseEntity<?> getUserByEmailIndexVer2(String str) {
        long start = System.currentTimeMillis();

        indexRepository.findByEmailContaining(str);

        long end = System.currentTimeMillis();
        // System.out.println("조회 시간(인덱스): " + (end - start) + "ms");

        return ResponseEntity.ok().body("조회 시간(인덱스): " + (end - start) + "ms");
    }
}
