package com.nga.amrest.fastDoc.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nga.amrest.fastDoc.config.DBConfiguration;
import com.nga.amrest.fastDoc.model.Companies;
import com.nga.amrest.fastDoc.model.ConfigurableColumns;
import com.nga.amrest.fastDoc.model.ConfigurableTables;
import com.nga.amrest.fastDoc.model.Countries;
import com.nga.amrest.fastDoc.model.Fields;
import com.nga.amrest.fastDoc.model.Groups;
import com.nga.amrest.fastDoc.model.MapGroupTemplates;
import com.nga.amrest.fastDoc.model.MapTemplateCriteriaValues;
import com.nga.amrest.fastDoc.model.MapTemplateFields;
import com.nga.amrest.fastDoc.model.Operators;
import com.nga.amrest.fastDoc.model.TemplateFieldTag;
import com.nga.amrest.fastDoc.model.Templates;
import com.nga.amrest.fastDoc.service.CodelistService;
import com.nga.amrest.fastDoc.service.CodelistTextService;
import com.nga.amrest.fastDoc.service.CompaniesService;
import com.nga.amrest.fastDoc.service.ConfigurableColumnsService;
import com.nga.amrest.fastDoc.service.ConfigurableTablesService;
import com.nga.amrest.fastDoc.service.CountryFastDocService;
import com.nga.amrest.fastDoc.service.EntitiesService;
import com.nga.amrest.fastDoc.service.FieldsService;
import com.nga.amrest.fastDoc.service.GroupsService;
import com.nga.amrest.fastDoc.service.MapCountryCompanyGroupService;
import com.nga.amrest.fastDoc.service.MapGroupTemplatesService;
import com.nga.amrest.fastDoc.service.MapRuleFieldsService;
import com.nga.amrest.fastDoc.service.MapTemplateCriteriaValuesService;
import com.nga.amrest.fastDoc.service.MapTemplateFieldsService;
import com.nga.amrest.fastDoc.service.OperatorsService;
import com.nga.amrest.fastDoc.service.RulesService;
import com.nga.amrest.fastDoc.service.TemplateFastDocService;
import com.nga.amrest.fastDoc.service.TemplateFieldTagService;
import com.nga.amrest.fastDoc.utility.WordFileProcessing;

@RestController
@RequestMapping("/DocGen/docGenAdmin/configurator")
public class Configurator {
	Logger logger = LoggerFactory.getLogger(DocGen.class);

	@Autowired
	MapCountryCompanyGroupService mapCountryCompanyGroupService;

	@Autowired
	MapGroupTemplatesService mapGroupTemplateService;

	@Autowired
	FieldsService fieldsService;

	@Autowired
	TemplateFastDocService templateService;

	@Autowired
	EntitiesService entitiesService;

	@Autowired
	MapRuleFieldsService mapRuleFieldsService;

	@Autowired
	RulesService rulesService;

	@Autowired
	MapTemplateFieldsService mapTemplateFieldsService;

	@Autowired
	CodelistTextService codelistTextService;

	@Autowired
	CodelistService codelistService;

	@Autowired
	ConfigurableTablesService configurableTablesService;

	@Autowired
	ConfigurableColumnsService configurableColumnsService;

	@Autowired
	CountryFastDocService countryService;

	@Autowired
	GroupsService groupsService;

	@Autowired
	CompaniesService companiesService;

	@Autowired
	MapTemplateCriteriaValuesService mapTemplateCriteriaValuesService;

	@Autowired
	OperatorsService operatorsService;

	@Autowired
	TemplateFieldTagService templateFieldTagService;

	@GetMapping(value = "/getConfigurableTables")
	public ResponseEntity<?> getTableNames(HttpServletRequest request) {

		try {
			HttpSession session = request.getSession(false);// false is not create new session and use the existing
			if (session.getAttribute("loginStatus") == null) {
				return new ResponseEntity<>("Session timeout! Please Login again!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			/*
			 *** Security Check *** Checking if user trying to login is exactly an Admin or
			 * not
			 *
			 */
			else if (session.getAttribute("adminLoginStatus") == null) {
				logger.error("Unauthorized access! User:" + (String) session.getAttribute("loggedInUser")
						+ ", which is not an admin in SF, tried to access TableNames in configurator App.");
				return new ResponseEntity<>(
						"Error! You are not authorized to access this resource! This event has been logged!",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return ResponseEntity.ok().body(configurableTablesService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/getTableData")
	public ResponseEntity<?> getTableData(@RequestParam(name = "tableID") String tableID,
			@RequestParam(value = "tableId", required = false) String tableId, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);// false is not create new session and use the existing
			if (session.getAttribute("loginStatus") == null) {
				return new ResponseEntity<>("Session timeout! Please Login again!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			/*
			 *** Security Check *** Checking if user trying to login is exactly an Admin or
			 * not
			 *
			 */
			else if (session.getAttribute("adminLoginStatus") == null) {
				logger.error("Unauthorized access! User:" + (String) session.getAttribute("loggedInUser")
						+ ", which is not an admin in SF, tried to access TableNames in configurator App.");
				return new ResponseEntity<>(
						"Error! You are not authorized to access this resource! This event has been logged!",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			ConfigurableTables configurableTables = configurableTablesService.findById(tableID);
			if (configurableTables == null)
				return new ResponseEntity<>("Error! Table not found.", HttpStatus.INTERNAL_SERVER_ERROR);

			List<ConfigurableColumns> configurableColumns = configurableColumnsService
					.findByTableID(configurableTables.getId());// retrieve all the configurable columns for the table
			// get names list of all the configurable columns
			List<String> columnNames = configurableColumnsService.getColumnNamesByTableID(configurableTables.getId());
			String tablePath = configurableTables.getPath(); // Table path
			JSONObject response = new JSONObject();
			JSONArray responseDataArray = new JSONArray();
			JSONObject tempObj = new JSONObject();
			JSONObject tempJsonObj = new JSONObject();
			String tempColumnName;
			switch (tablePath) {
			case DBConfiguration.COUNTRIES:
				List<Countries> countries = countryService.findAll();
				for (int i = 0; i < countries.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(countries.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempObj.put(columnNames.get(j), tempJsonObj.get(columnNames.get(j)));
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			case DBConfiguration.GROUPS:
				List<Groups> groups = groupsService.findAll();
				for (int i = 0; i < groups.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(groups.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			case DBConfiguration.COMPANIES:
				List<Companies> companies = companiesService.findAll();
				for (int i = 0; i < companies.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(companies.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());

			case DBConfiguration.MAP_TEMPLATE_CRITERIA_VALUES:
				List<MapTemplateCriteriaValues> mapTemplateCriteriaValues = mapTemplateCriteriaValuesService.findAll();
				for (int i = 0; i < mapTemplateCriteriaValues.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(mapTemplateCriteriaValues.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			case DBConfiguration.OPERATORS:
				List<Operators> operators = operatorsService.findAll();
				for (int i = 0; i < operators.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(operators.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			case DBConfiguration.TEMPLATE_FIELD_TAG:
				List<TemplateFieldTag> templateFieldTag = templateFieldTagService.findAll();
				for (int i = 0; i < templateFieldTag.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(templateFieldTag.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			case DBConfiguration.FIELDS:
				List<Fields> fields = fieldsService.findAll();
				for (int i = 0; i < fields.size(); i++) {
					tempObj = new JSONObject();
					tempJsonObj = new JSONObject(fields.get(i).toString());
					for (int j = 0; j < columnNames.size(); j++) {
						tempColumnName = columnNames.get(j);
						tempObj.put(tempColumnName,
								tempJsonObj.has(tempColumnName) ? tempJsonObj.get(columnNames.get(j)) : "");
					}
					responseDataArray.put(tempObj);
				}
				response.put("columns", configurableColumns);
				response.put("data", responseDataArray);
				return ResponseEntity.ok().body(response.toString());
			default:
				// code block
				return ResponseEntity.ok().body(configurableTablesService.findAll());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/uploadTemplate", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam(name = "templateName") String templateName,
			@RequestParam(name = "description") String description,
			@RequestParam(name = "displayName") String displayName, @RequestParam(name = "countryId") String countryId,
			@RequestParam(name = "companyId") String companyId, @RequestParam(name = "groupId") String groupId,
			@RequestParam("file") MultipartFile multipartFile, HttpSession session) {
		// function required to Create Template from Configurator App
		try {

			if (session.getAttribute("loginStatus") == null) {
				return new ResponseEntity<>("Session timeout! Please Login again!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			/*
			 *** Security Check *** Checking if user trying to login is exactly an Admin or
			 * not
			 *
			 */
			else if (session.getAttribute("adminLoginStatus") == null) {
				logger.error("Unauthorized access! User:" + (String) session.getAttribute("loggedInUser")
						+ ", which is not an admin in SF, tried to access TableNames in configurator App.");
				return new ResponseEntity<>(
						"Error! You are not authorized to access this resource! This event has been logged!",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			if (mapCountryCompanyGroupService.findByGroupCountryCompany(groupId, countryId, companyId).size() == 0)
				return new ResponseEntity<>(
						"Error! Failed! No corresponding entry for provided Country Company and Group.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			// byte bytesArr[] = null;
			String fileName = multipartFile.getOriginalFilename();

			logger.debug("Uploaded Orignal FileName: " + fileName + " ::: fileName:" + multipartFile.getName()
					+ " ::: contentType:" + multipartFile.getContentType());
			Templates generatedTemplate = _createTemplate(templateName, description, displayName);
			JSONArray tags = WordFileProcessing.getTags(WordFileProcessing.createWordFile(multipartFile));
			_mapGroupTemplate(generatedTemplate, groupId);
			logger.debug(tags.toString());
			_mapTemplateFields(generatedTemplate, tags);
			return ResponseEntity.ok().body(generatedTemplate.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/mapTemplateCriteriaValues", method = RequestMethod.POST)
	public ResponseEntity<?> mapTemplateCriteriaValues(@RequestParam(name = "templateId") String templateId,
			@RequestBody String requestData, HttpSession session) {
		try {

			if (session.getAttribute("loginStatus") == null) {
				return new ResponseEntity<>("Session timeout! Please Login again!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			/*
			 *** Security Check *** Checking if user trying to login is exactly an Admin or
			 * not
			 *
			 */
			else if (session.getAttribute("adminLoginStatus") == null) {
				logger.error("Unauthorized access! User:" + (String) session.getAttribute("loggedInUser")
						+ ", which is not an admin in SF, tried to access /mapTemplateCriteriaValues endpoint in configurator App.");
				return new ResponseEntity<>(
						"Error! You are not authorized to access this resource! This event has been logged!",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			JSONObject requestObj = new JSONObject(requestData);
			JSONArray requestDataArr = requestObj.getJSONArray("data");
			JSONObject tempObj;
			for (int i = 0; i < requestDataArr.length(); i++) {
				tempObj = requestDataArr.getJSONObject(i);
				MapTemplateCriteriaValues mapTemplateCriteriaValues = new MapTemplateCriteriaValues();
				mapTemplateCriteriaValues.setTemplateId(templateId);
				mapTemplateCriteriaValues.setFieldId(tempObj.getString("fieldId"));
				mapTemplateCriteriaValues.setValue(tempObj.getString("value"));
				mapTemplateCriteriaValues.setOperatorId(tempObj.getString("operatorId"));
				mapTemplateCriteriaValuesService.create(mapTemplateCriteriaValues);
			}
			return ResponseEntity.ok().body("Success! All criteria fields Mapped.");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getCompaniesAndGroupsFromCountry", method = RequestMethod.GET)
	public ResponseEntity<?> getCompaniesAndGroupsFromCountry(@RequestParam(name = "countryID") String countryID,
			HttpSession session) {
		try {
			if (session.getAttribute("loginStatus") == null) {
				return new ResponseEntity<>("Session timeout! Please Login again!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			/*
			 *** Security Check *** Checking if user trying to login is exactly an Admin or
			 * not
			 *
			 */
			else if (session.getAttribute("adminLoginStatus") == null) {
				logger.error("Unauthorized access! User:" + (String) session.getAttribute("loggedInUser")
						+ ", which is not an admin in SF, tried to access getCompaniesAndGroupsFromCountry Endpoint in configurator App.");
				return new ResponseEntity<>(
						"Error! You are not authorized to access this resource! This event has been logged!",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return ResponseEntity.ok().body(mapCountryCompanyGroupService.findByCountry(countryID));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Templates _createTemplate(String templateName, String description, String displayName) {
		Templates newTemplate = new Templates();
		String templateId = _getUUID();
		newTemplate.setId(templateId);
		newTemplate.setName(templateName);
		newTemplate.setDescription(description);
		newTemplate.setDisplayName(displayName);
		return templateService.create(newTemplate);
	}

	private MapGroupTemplates _mapGroupTemplate(Templates generatedTemplate, String groupId) {
		MapGroupTemplates mapGroupTemplates = new MapGroupTemplates();
		mapGroupTemplates.setTemplateID(generatedTemplate.getId());
		mapGroupTemplates.setGroupID(groupId);
		return mapGroupTemplateService.create(mapGroupTemplates);
	}

	private String _getUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(0, uuid.length() - 4);
		return uuid;
	}

	private Boolean _mapTemplateFields(Templates template, JSONArray tags) {
		try {
			MapTemplateFields templateField;
			for (int i = 0; i < tags.length(); i++) {
				templateField = new MapTemplateFields();
				templateField.setTemplateID(template.getId());
				templateField.setTemplateFieldTagId(tags.getString(i));
				mapTemplateFieldsService.create(templateField);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}