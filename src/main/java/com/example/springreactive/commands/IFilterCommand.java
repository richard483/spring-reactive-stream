package com.example.springreactive.commands;

import com.blibli.oss.backend.command.Command;
import com.example.springreactive.requests.FilterCommandRequest;
import com.example.springreactive.responses.FilterCommandResponse;

public interface IFilterCommand extends Command<FilterCommandRequest, FilterCommandResponse> {

}
