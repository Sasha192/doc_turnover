package app.tenantdefault.service;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component("doc_uuid_mongodb_gen")
public class DocumentEntityGenerator implements IUuidGenerator {

    private TimeBasedGenerator generator;

    public DocumentEntityGenerator() {
        generator = Generators
                .timeBasedGenerator(
                        EthernetAddress.fromInterface()
                );
    }

    @Override
    public UUID generate() {
        return generator.generate();
    }
}
