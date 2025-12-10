import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './identity-document.reducer';

export const IdentityDocument = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const identityDocumentList = useAppSelector(state => state.identityDocument.entities);
  const loading = useAppSelector(state => state.identityDocument.loading);
  const totalItems = useAppSelector(state => state.identityDocument.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="identity-document-heading" data-cy="IdentityDocumentHeading">
        <Translate contentKey="monsterdamChicksApp.identityDocument.home.title">Identity Documents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.identityDocument.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/identity-document/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.identityDocument.home.createLabel">Create new Identity Document</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {identityDocumentList && identityDocumentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('documentName')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.documentName">Document Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentName')} />
                </th>
                <th className="hand" onClick={sort('documentDescription')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.documentDescription">Document Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentDescription')} />
                </th>
                <th className="hand" onClick={sort('documentStatus')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.documentStatus">Document Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentStatus')} />
                </th>
                <th className="hand" onClick={sort('documentType')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.documentType">Document Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentType')} />
                </th>
                <th className="hand" onClick={sort('fileDocument')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.fileDocument">File Document</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileDocument')} />
                </th>
                <th className="hand" onClick={sort('fileDocumentS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.fileDocumentS3Key">File Document S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileDocumentS3Key')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.identityDocument.review">Review</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {identityDocumentList.map((identityDocument, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/identity-document/${identityDocument.id}`} color="link" size="sm">
                      {identityDocument.id}
                    </Button>
                  </td>
                  <td>{identityDocument.documentName}</td>
                  <td>{identityDocument.documentDescription}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.DocumentStatus.${identityDocument.documentStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.DocumentType.${identityDocument.documentType}`} />
                  </td>
                  <td>
                    {identityDocument.fileDocument ? (
                      <div>
                        {identityDocument.fileDocumentContentType ? (
                          <a onClick={openFile(identityDocument.fileDocumentContentType, identityDocument.fileDocument)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {identityDocument.fileDocumentContentType}, {byteSize(identityDocument.fileDocument)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{identityDocument.fileDocumentS3Key}</td>
                  <td>
                    {identityDocument.createdDate ? (
                      <TextFormat type="date" value={identityDocument.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {identityDocument.lastModifiedDate ? (
                      <TextFormat type="date" value={identityDocument.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{identityDocument.createdBy}</td>
                  <td>{identityDocument.lastModifiedBy}</td>
                  <td>
                    {identityDocument.review ? (
                      <Link to={`/identity-document-review/${identityDocument.review.id}`}>{identityDocument.review.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/identity-document/${identityDocument.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/identity-document/${identityDocument.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/identity-document/${identityDocument.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.identityDocument.home.notFound">No Identity Documents found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={identityDocumentList && identityDocumentList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default IdentityDocument;
