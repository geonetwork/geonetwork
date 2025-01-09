/* tslint:disable */
/* eslint-disable */
/**
 * GeoNetwork API
 * This API exposes endpoints to GeoNetwork API.
 *
 * The version of the OpenAPI document: 5.0.0
 * Contact: geonetwork-users@lists.sourceforge.net
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import * as runtime from '../runtime';
import type {
  AnalysisSynch200Response,
  AttributeStatistics,
  DataFormat,
} from '../models/index';
import {
    AnalysisSynch200ResponseFromJSON,
    AnalysisSynch200ResponseToJSON,
    AttributeStatisticsFromJSON,
    AttributeStatisticsToJSON,
    DataFormatFromJSON,
    DataFormatToJSON,
} from '../models/index';

export interface AnalysisSynchRequest {
    datasource: string;
    layer: string;
}

export interface AnalysisSynchMetadataResourceRequest {
    metadataUuid: string;
    visibility: AnalysisSynchMetadataResourceVisibilityEnum;
    datasource: string;
    approved: boolean;
    layer: string;
}

export interface ApplyDataAnalysisOnRecordRequest {
    uuid: string;
    datasource: string;
    layer: string;
}

export interface ApplyDataAnalysisOnRecordForMetadataResourceRequest {
    uuid: string;
    visibility: ApplyDataAnalysisOnRecordForMetadataResourceVisibilityEnum;
    datasource: string;
    approved: boolean;
    layer: string;
}

export interface AttributeCodelistRequest {
    datasource: string;
    layer: string;
    attribute: string;
    limit?: number;
}

export interface AttributeStatisticsRequest {
    datasource: string;
    layer: string;
    attribute: string;
}

export interface LayersRequest {
    datasource: string;
}

export interface LayersMetadataResourceRequest {
    metadataUuid: string;
    visibility: LayersMetadataResourceVisibilityEnum;
    datasource: string;
    approved: boolean;
}

export interface PreviewDataAnalysisOnRecordRequest {
    uuid: string;
    datasource: string;
    layer: string;
}

export interface PreviewDataAnalysisOnRecordForMetadataResourceRequest {
    metadataUuid: string;
    visibility: PreviewDataAnalysisOnRecordForMetadataResourceVisibilityEnum;
    datasource: string;
    approved: boolean;
    layer: string;
}

/**
 * 
 */
export class DataAnalysisControllerApi extends runtime.BaseAPI {

    /**
     */
    async analysisSynchRaw(requestParameters: AnalysisSynchRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<AnalysisSynch200Response>> {
        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling analysisSynch().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling analysisSynch().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/execute`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => AnalysisSynch200ResponseFromJSON(jsonValue));
    }

    /**
     */
    async analysisSynch(requestParameters: AnalysisSynchRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<AnalysisSynch200Response> {
        const response = await this.analysisSynchRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async analysisSynchMetadataResourceRaw(requestParameters: AnalysisSynchMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<AnalysisSynch200Response>> {
        if (requestParameters['metadataUuid'] == null) {
            throw new runtime.RequiredError(
                'metadataUuid',
                'Required parameter "metadataUuid" was null or undefined when calling analysisSynchMetadataResource().'
            );
        }

        if (requestParameters['visibility'] == null) {
            throw new runtime.RequiredError(
                'visibility',
                'Required parameter "visibility" was null or undefined when calling analysisSynchMetadataResource().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling analysisSynchMetadataResource().'
            );
        }

        if (requestParameters['approved'] == null) {
            throw new runtime.RequiredError(
                'approved',
                'Required parameter "approved" was null or undefined when calling analysisSynchMetadataResource().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling analysisSynchMetadataResource().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['metadataUuid'] != null) {
            queryParameters['metadataUuid'] = requestParameters['metadataUuid'];
        }

        if (requestParameters['visibility'] != null) {
            queryParameters['visibility'] = requestParameters['visibility'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['approved'] != null) {
            queryParameters['approved'] = requestParameters['approved'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/executeMetadataResource`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => AnalysisSynch200ResponseFromJSON(jsonValue));
    }

    /**
     */
    async analysisSynchMetadataResource(requestParameters: AnalysisSynchMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<AnalysisSynch200Response> {
        const response = await this.analysisSynchMetadataResourceRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async applyDataAnalysisOnRecordRaw(requestParameters: ApplyDataAnalysisOnRecordRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<string>> {
        if (requestParameters['uuid'] == null) {
            throw new runtime.RequiredError(
                'uuid',
                'Required parameter "uuid" was null or undefined when calling applyDataAnalysisOnRecord().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling applyDataAnalysisOnRecord().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling applyDataAnalysisOnRecord().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['uuid'] != null) {
            queryParameters['uuid'] = requestParameters['uuid'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/apply`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        if (this.isJsonMime(response.headers.get('content-type'))) {
            return new runtime.JSONApiResponse<string>(response);
        } else {
            return new runtime.TextApiResponse(response) as any;
        }
    }

    /**
     */
    async applyDataAnalysisOnRecord(requestParameters: ApplyDataAnalysisOnRecordRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<string> {
        const response = await this.applyDataAnalysisOnRecordRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async applyDataAnalysisOnRecordForMetadataResourceRaw(requestParameters: ApplyDataAnalysisOnRecordForMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<string>> {
        if (requestParameters['uuid'] == null) {
            throw new runtime.RequiredError(
                'uuid',
                'Required parameter "uuid" was null or undefined when calling applyDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['visibility'] == null) {
            throw new runtime.RequiredError(
                'visibility',
                'Required parameter "visibility" was null or undefined when calling applyDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling applyDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['approved'] == null) {
            throw new runtime.RequiredError(
                'approved',
                'Required parameter "approved" was null or undefined when calling applyDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling applyDataAnalysisOnRecordForMetadataResource().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['uuid'] != null) {
            queryParameters['uuid'] = requestParameters['uuid'];
        }

        if (requestParameters['visibility'] != null) {
            queryParameters['visibility'] = requestParameters['visibility'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['approved'] != null) {
            queryParameters['approved'] = requestParameters['approved'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/applyForMetadataResource`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        if (this.isJsonMime(response.headers.get('content-type'))) {
            return new runtime.JSONApiResponse<string>(response);
        } else {
            return new runtime.TextApiResponse(response) as any;
        }
    }

    /**
     */
    async applyDataAnalysisOnRecordForMetadataResource(requestParameters: ApplyDataAnalysisOnRecordForMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<string> {
        const response = await this.applyDataAnalysisOnRecordForMetadataResourceRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async attributeCodelistRaw(requestParameters: AttributeCodelistRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<object>>> {
        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling attributeCodelist().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling attributeCodelist().'
            );
        }

        if (requestParameters['attribute'] == null) {
            throw new runtime.RequiredError(
                'attribute',
                'Required parameter "attribute" was null or undefined when calling attributeCodelist().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        if (requestParameters['attribute'] != null) {
            queryParameters['attribute'] = requestParameters['attribute'];
        }

        if (requestParameters['limit'] != null) {
            queryParameters['limit'] = requestParameters['limit'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/attribute/codelist`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse<any>(response);
    }

    /**
     */
    async attributeCodelist(requestParameters: AttributeCodelistRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<object>> {
        const response = await this.attributeCodelistRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async attributeStatisticsRaw(requestParameters: AttributeStatisticsRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<AttributeStatistics>>> {
        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling attributeStatistics().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling attributeStatistics().'
            );
        }

        if (requestParameters['attribute'] == null) {
            throw new runtime.RequiredError(
                'attribute',
                'Required parameter "attribute" was null or undefined when calling attributeStatistics().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        if (requestParameters['attribute'] != null) {
            queryParameters['attribute'] = requestParameters['attribute'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/attribute/statistics`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => jsonValue.map(AttributeStatisticsFromJSON));
    }

    /**
     */
    async attributeStatistics(requestParameters: AttributeStatisticsRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<AttributeStatistics>> {
        const response = await this.attributeStatisticsRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async formatsRaw(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<DataFormat>>> {
        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/formats`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => jsonValue.map(DataFormatFromJSON));
    }

    /**
     */
    async formats(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<DataFormat>> {
        const response = await this.formatsRaw(initOverrides);
        return await response.value();
    }

    /**
     */
    async layersRaw(requestParameters: LayersRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<string>>> {
        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling layers().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/layers`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse<any>(response);
    }

    /**
     */
    async layers(requestParameters: LayersRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<string>> {
        const response = await this.layersRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async layersMetadataResourceRaw(requestParameters: LayersMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<string>>> {
        if (requestParameters['metadataUuid'] == null) {
            throw new runtime.RequiredError(
                'metadataUuid',
                'Required parameter "metadataUuid" was null or undefined when calling layersMetadataResource().'
            );
        }

        if (requestParameters['visibility'] == null) {
            throw new runtime.RequiredError(
                'visibility',
                'Required parameter "visibility" was null or undefined when calling layersMetadataResource().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling layersMetadataResource().'
            );
        }

        if (requestParameters['approved'] == null) {
            throw new runtime.RequiredError(
                'approved',
                'Required parameter "approved" was null or undefined when calling layersMetadataResource().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['metadataUuid'] != null) {
            queryParameters['metadataUuid'] = requestParameters['metadataUuid'];
        }

        if (requestParameters['visibility'] != null) {
            queryParameters['visibility'] = requestParameters['visibility'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['approved'] != null) {
            queryParameters['approved'] = requestParameters['approved'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/layersMetadataResource`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse<any>(response);
    }

    /**
     */
    async layersMetadataResource(requestParameters: LayersMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<string>> {
        const response = await this.layersMetadataResourceRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async previewDataAnalysisOnRecordRaw(requestParameters: PreviewDataAnalysisOnRecordRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<string>> {
        if (requestParameters['uuid'] == null) {
            throw new runtime.RequiredError(
                'uuid',
                'Required parameter "uuid" was null or undefined when calling previewDataAnalysisOnRecord().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling previewDataAnalysisOnRecord().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling previewDataAnalysisOnRecord().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['uuid'] != null) {
            queryParameters['uuid'] = requestParameters['uuid'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/preview`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        if (this.isJsonMime(response.headers.get('content-type'))) {
            return new runtime.JSONApiResponse<string>(response);
        } else {
            return new runtime.TextApiResponse(response) as any;
        }
    }

    /**
     */
    async previewDataAnalysisOnRecord(requestParameters: PreviewDataAnalysisOnRecordRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<string> {
        const response = await this.previewDataAnalysisOnRecordRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async previewDataAnalysisOnRecordForMetadataResourceRaw(requestParameters: PreviewDataAnalysisOnRecordForMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<string>> {
        if (requestParameters['metadataUuid'] == null) {
            throw new runtime.RequiredError(
                'metadataUuid',
                'Required parameter "metadataUuid" was null or undefined when calling previewDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['visibility'] == null) {
            throw new runtime.RequiredError(
                'visibility',
                'Required parameter "visibility" was null or undefined when calling previewDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['datasource'] == null) {
            throw new runtime.RequiredError(
                'datasource',
                'Required parameter "datasource" was null or undefined when calling previewDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['approved'] == null) {
            throw new runtime.RequiredError(
                'approved',
                'Required parameter "approved" was null or undefined when calling previewDataAnalysisOnRecordForMetadataResource().'
            );
        }

        if (requestParameters['layer'] == null) {
            throw new runtime.RequiredError(
                'layer',
                'Required parameter "layer" was null or undefined when calling previewDataAnalysisOnRecordForMetadataResource().'
            );
        }

        const queryParameters: any = {};

        if (requestParameters['metadataUuid'] != null) {
            queryParameters['metadataUuid'] = requestParameters['metadataUuid'];
        }

        if (requestParameters['visibility'] != null) {
            queryParameters['visibility'] = requestParameters['visibility'];
        }

        if (requestParameters['datasource'] != null) {
            queryParameters['datasource'] = requestParameters['datasource'];
        }

        if (requestParameters['approved'] != null) {
            queryParameters['approved'] = requestParameters['approved'];
        }

        if (requestParameters['layer'] != null) {
            queryParameters['layer'] = requestParameters['layer'];
        }

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/previewForMetadataResource`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        if (this.isJsonMime(response.headers.get('content-type'))) {
            return new runtime.JSONApiResponse<string>(response);
        } else {
            return new runtime.TextApiResponse(response) as any;
        }
    }

    /**
     */
    async previewDataAnalysisOnRecordForMetadataResource(requestParameters: PreviewDataAnalysisOnRecordForMetadataResourceRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<string> {
        const response = await this.previewDataAnalysisOnRecordForMetadataResourceRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     */
    async statusRaw(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<string>> {
        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/data/analysis/status`,
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        if (this.isJsonMime(response.headers.get('content-type'))) {
            return new runtime.JSONApiResponse<string>(response);
        } else {
            return new runtime.TextApiResponse(response) as any;
        }
    }

    /**
     */
    async status(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<string> {
        const response = await this.statusRaw(initOverrides);
        return await response.value();
    }

}

/**
 * @export
 */
export const AnalysisSynchMetadataResourceVisibilityEnum = {
    Public: 'public',
    Private: 'private'
} as const;
export type AnalysisSynchMetadataResourceVisibilityEnum = typeof AnalysisSynchMetadataResourceVisibilityEnum[keyof typeof AnalysisSynchMetadataResourceVisibilityEnum];
/**
 * @export
 */
export const ApplyDataAnalysisOnRecordForMetadataResourceVisibilityEnum = {
    Public: 'public',
    Private: 'private'
} as const;
export type ApplyDataAnalysisOnRecordForMetadataResourceVisibilityEnum = typeof ApplyDataAnalysisOnRecordForMetadataResourceVisibilityEnum[keyof typeof ApplyDataAnalysisOnRecordForMetadataResourceVisibilityEnum];
/**
 * @export
 */
export const LayersMetadataResourceVisibilityEnum = {
    Public: 'public',
    Private: 'private'
} as const;
export type LayersMetadataResourceVisibilityEnum = typeof LayersMetadataResourceVisibilityEnum[keyof typeof LayersMetadataResourceVisibilityEnum];
/**
 * @export
 */
export const PreviewDataAnalysisOnRecordForMetadataResourceVisibilityEnum = {
    Public: 'public',
    Private: 'private'
} as const;
export type PreviewDataAnalysisOnRecordForMetadataResourceVisibilityEnum = typeof PreviewDataAnalysisOnRecordForMetadataResourceVisibilityEnum[keyof typeof PreviewDataAnalysisOnRecordForMetadataResourceVisibilityEnum];