/*
SPDX-License-Identifier: Apache-2.0
*/

package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric-contract-api-go/contractapi"
	"strconv"
)

// SmartContract provides functions for managing a classgrade
type SmartContract struct {
	contractapi.Contract
}

// ClasGrade describes basic details of what makes up a classgrade
type ClassGrade struct {
	Sid         int     `json:"sid"`
	Grade       float64 `json:"grade"`
	MakeupGrade float64 `json:"makeupgrade"`
	ClassName   string  `json:"classname"`
}

// InitLedger adds a base set of classgrades to the ledger
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	return nil
}

// CreateClassGrade adds a new classgrade to the world state with given details
func (s *SmartContract) CreateClassGrade(ctx contractapi.TransactionContextInterface, sid int, grade float64, makeupGrade float64, className string) error {
	classGrade := ClassGrade{
		Sid:         sid,
		Grade:       grade,
		MakeupGrade: makeupGrade,
		ClassName:   className,
	}

	classGradeAsBytes, _ := json.Marshal(classGrade)

	key, err := ctx.GetStub().CreateCompositeKey("CLASSGRADE", []string{classGrade.ClassName, strconv.Itoa(classGrade.Sid)})

	if err != nil {
		return fmt.Errorf("Failed to put to world state. %s", err.Error())
	}

	return ctx.GetStub().PutState(key, classGradeAsBytes)
}

// QueryClassGrade returns the classgrade stored in the world state with given id
func (s *SmartContract) QueryClassGrade(ctx contractapi.TransactionContextInterface, className string, sid int) (*ClassGrade, error) {

	key, err := ctx.GetStub().CreateCompositeKey("CLASSGRADE", []string{className, strconv.Itoa(sid)})

	if err != nil {
		return nil, fmt.Errorf("Failed to put to world state. %s", err.Error())
	}

	classGradeAsBytes, err := ctx.GetStub().GetState(key)

	if err != nil {
		return nil, fmt.Errorf("Failed to read from world state. %s", err.Error())
	}

	if classGradeAsBytes == nil {
		return nil, fmt.Errorf("%s, %d does not exist", className, sid)
	}

	classGrade := new(ClassGrade)
	_ = json.Unmarshal(classGradeAsBytes, classGrade)

	return classGrade, nil
}

// QueryAllClassGrades returns all grades found in world state
func (s *SmartContract) QueryAllClassGrades(ctx contractapi.TransactionContextInterface) ([]ClassGrade, error) {
	startKey := ""
	endKey := ""

	resultsIterator, err := ctx.GetStub().GetStateByRange(startKey, endKey)

	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	results := []ClassGrade{}

	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()

		if err != nil {
			return nil, err
		}

		classGrade := new(ClassGrade)
		_ = json.Unmarshal(queryResponse.Value, classGrade)

		results = append(results, *classGrade)
	}

	return results, nil
}

// ChangeGrade updates the owner field of car with given id in world state
func (s *SmartContract) ChangeGrade(ctx contractapi.TransactionContextInterface, className string, sid int, newGrade float64) error {

	key, err := ctx.GetStub().CreateCompositeKey("CLASSGRADE", []string{className, strconv.Itoa(sid)})

	if err != nil {
		return fmt.Errorf("Failed to put to world state. %s", err.Error())
	}

	classGrade, err := s.QueryClassGrade(ctx, className, sid)

	
	if err != nil {
		classGrade = new(ClassGrade)
		classGrade.ClassName = className
		classGrade.Sid = sid
		classGrade.MakeupGrade = -1
	}

	classGrade.Grade = newGrade

	classGradeAsBytes, _ := json.Marshal(classGrade)

	return ctx.GetStub().PutState(key, classGradeAsBytes)
}

// ChangeGrade updates the owner field of car with given id in world state
func (s *SmartContract) ChangeMakeupGrade(ctx contractapi.TransactionContextInterface, className string, sid int, newMakeupGrade float64) error {
	
	key, err := ctx.GetStub().CreateCompositeKey("CLASSGRADE", []string{className, strconv.Itoa(sid)})

	if err != nil {
		return fmt.Errorf("Failed to put to world state. %s", err.Error())
	}
	classGrade, err := s.QueryClassGrade(ctx, className, sid)

	if err != nil {
		classGrade = new(ClassGrade)
		classGrade.ClassName = className
		classGrade.Sid = sid
		classGrade.Grade = -1
	}

	classGrade.MakeupGrade = newMakeupGrade

	classGradeAsBytes, _ := json.Marshal(classGrade)

	return ctx.GetStub().PutState(key, classGradeAsBytes)
}

func main() {

	chaincode, err := contractapi.NewChaincode(new(SmartContract))

	if err != nil {
		fmt.Printf("Error create fabcar chaincode: %s", err.Error())
		return
	}

	if err := chaincode.Start(); err != nil {
		fmt.Printf("Error starting fabcar chaincode: %s", err.Error())
	}
}
