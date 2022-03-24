package com.netocampana.springbootessentials.exceptions;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ResourceNotFoundDetails extends ExceptionDetails{

}
